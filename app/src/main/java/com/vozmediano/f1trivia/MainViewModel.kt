package com.vozmediano.f1trivia

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vozmediano.f1trivia.domain.F1Repository
import com.vozmediano.f1trivia.domain.model.f1.Circuit
import com.vozmediano.f1trivia.domain.model.f1.Constructor
import com.vozmediano.f1trivia.domain.model.f1.Driver
import com.vozmediano.f1trivia.domain.model.quiz.Option
import com.vozmediano.f1trivia.domain.model.quiz.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(val f1Repository: F1Repository) : ViewModel() {

    //QUESTION
    private val _question = MutableStateFlow<Question?>(null)
    val question: StateFlow<Question?> = _question.asStateFlow()

    //DRIVERS
    private val _driver = MutableStateFlow<Driver?>(null)
    val driver: StateFlow<Driver?> = _driver.asStateFlow()

    private val _drivers = MutableStateFlow<List<Driver>?>(null)
    val drivers: StateFlow<List<Driver>?> = _drivers.asStateFlow()

    //CONSTRUCTORS
    private val _constructor = MutableStateFlow<Constructor?>(null)
    val constructor: StateFlow<Constructor?> = _constructor.asStateFlow()

    private val _constructors = MutableStateFlow<List<Constructor>?>(null)
    val constructors: StateFlow<List<Constructor>?> = _constructors.asStateFlow()

    //CIRCUITS
    private val _circuit = MutableStateFlow<Circuit?>(null)
    val circuit: StateFlow<Circuit?> = _circuit.asStateFlow()

    private val _circuits = MutableStateFlow<List<Circuit>?>(null)
    val circuits: StateFlow<List<Circuit>?> = _circuits.asStateFlow()

    //QUESTION
    fun fetchQuestionDriverBySeasonAndCircuitAndPosition() {
        viewModelScope.launch {
            val correctYear = (2003..2024).random().toString()

            // Fetch circuits for the selected season
            val circuits = try {
                f1Repository.getCircuitsBySeason(correctYear)
            } catch (e: Exception) {
                Log.e("ViewModel", "Failed to fetch circuits: ${e.message}")
                _question.value = null
                return@launch
            }

            // Check if circuits are empty
            if (circuits.isEmpty()) {
                Log.e("ViewModel", "No circuits found for year $correctYear")
                _question.value = null
                return@launch
            }

            // Randomly select a circuit from the available circuits
            val correctCircuit = circuits.random().circuitId

            // Prepare the question object
            val question = Question(
                "Who won the ${correctCircuit.replaceFirstChar { it.uppercase() }} GP in $correctYear?",
                mutableListOf()
            )

            try {
                // Fetch correct driver for the circuit and year
                val correctDriver = try {
                    f1Repository.getDriverBySeasonAndCircuitAndPosition(
                        correctYear,
                        correctCircuit,
                        "1"
                    )
                } catch (e: Exception) {
                    Log.e("ViewModel", "Correct driver fetch failed: ${e.message}")
                    _question.value = null
                    return@launch
                }

                // Add the correct driver to the options
                question.options.add(
                    Option(
                        id = 0,
                        shortText = "${correctDriver.givenName} ${correctDriver.familyName}",
                        longText = "${correctDriver.givenName} ${correctDriver.familyName} won the $correctCircuit GP in $correctYear",
                        isCorrect = true
                    )
                )

                // Fetch 3 wrong drivers as distractors from different years and circuits
                val allCircuits = listOf("monaco", "spa", "suzuka", "silverstone", "interlagos", "monza")
                val distractorOptions = mutableSetOf<String>() // To avoid duplicates
                val wrongDrivers = mutableListOf<Option>()

                while (wrongDrivers.size < 3) {
                    val circuit = allCircuits.random()
                    val year = (2003..2024).random().toString()
                    val key = "$year-$circuit"
                    if (key == "$correctYear-$correctCircuit" || !distractorOptions.add(key)) continue

                    try {
                        val driver = f1Repository.getDriverBySeasonAndCircuitAndPosition(
                            year,
                            circuit,
                            "1"
                        )
                        wrongDrivers.add(
                            Option(
                                id = wrongDrivers.size + 1,
                                shortText = "${driver.givenName} ${driver.familyName}",
                                longText = "${driver.givenName} ${driver.familyName} won the $circuit GP in $year",
                                isCorrect = false
                            )
                        )
                    } catch (e: Exception) {
                        Log.w(
                            "ViewModel",
                            "Skipping invalid distractor: $key (${e.message})"
                        )
                    }
                }

                // Add all wrong drivers to the options
                question.options.addAll(wrongDrivers)

                // Shuffle the options to randomize the answer positions
                question.options.shuffle()

                // Set the question to the state
                _question.value = question

            } catch (e: Exception) {
                Log.e("ViewModel", "Unexpected error while generating question: ${e.message}")
                _question.value = null
            }
        }
    }


    //DRIVERS
    fun fetchDrivers() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val drivers = f1Repository.getDrivers()
                _drivers.value = drivers
                Log.i("Tests", "Drivers: $drivers")
            } catch (e: retrofit2.HttpException) {
                Log.i("Tests", "HTTP error: ${e.code()} - ${e.message()}")
            } catch (e: Exception) {
                Log.i("Tests", "Error fetching drivers: ${e.message.orEmpty()}")
            }
        }
    }

    fun fetchDriverById(driverId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val driver = f1Repository.getDriverById(driverId)
                _driver.value = driver
                Log.i("Tests", "(VM) Driver: $driver")
            } catch (e: retrofit2.HttpException) {
                Log.i("Tests", "(VM) HTTP error: ${e.code()} - ${e.message()}")
            } catch (e: Exception) {
                Log.i("Tests", "(VM) Error fetching driver: ${e.message.orEmpty()}")
            }
        }
    }

    fun fetchDriversBySeason(season: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val drivers = f1Repository.getDriversBySeason(season)
                _drivers.value = drivers
                Log.i("Tests", "Drivers: $drivers")
            } catch (e: retrofit2.HttpException) {
                Log.i("Tests", "HTTP error: ${e.code()} - ${e.message()}")
            } catch (e: Exception) {
                Log.i("Tests", "Error fetching drivers: ${e.message.orEmpty()}")
            }
        }
    }

    private fun fetchDriverBySeasonAndCircuitAndPosition(
        season: String,
        circuit: String,
        position: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val driver =
                    f1Repository.getDriverBySeasonAndCircuitAndPosition(season, circuit, position)
                _driver.value = driver
            } catch (e: Exception) {
                Log.i("Tests", "Error fetching driver: ${e.message.orEmpty()}")
            }
        }
    }


    //CONSTRUCTORS
    fun fetchConstructors() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val constructors = f1Repository.getConstructors()
                _constructors.value = constructors
                Log.i("Tests", "Constructors: $constructors")
            } catch (e: retrofit2.HttpException) {
                Log.i("Tests", "HTTP error: ${e.code()} - ${e.message()}")
            } catch (e: Exception) {
                Log.i("Tests", "Error fetching constructors: ${e.message.orEmpty()}")
            }
        }

    }

    fun fetchConstructorById(constructorId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val constructor = f1Repository.getConstructorById(constructorId)
                _constructor.value = constructor
                Log.i("Tests", "Constructor: $constructor")
            } catch (e: retrofit2.HttpException) {
                Log.i("Tests", "HTTP error: ${e.code()} - ${e.message()}")
            } catch (e: Exception) {
                Log.i("Tests", "Error fetching constructor: ${e.message.orEmpty()}")
            }
        }
    }

    fun fetchConstructorsBySeason(season: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val constructors = f1Repository.getConstructorsBySeason(season)
                _constructors.value = constructors
                Log.i("Tests", "Constructors: $constructors")
            } catch (e: retrofit2.HttpException) {
                Log.i("Tests", "HTTP error: ${e.code()} - ${e.message()}")
            } catch (e: Exception) {
                Log.i("Tests", "Error fetching constructors: ${e.message.orEmpty()}")
            }
        }
    }

    //CIRCUITS
    fun fetchCircuits() {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val circuits = f1Repository.getCircuits()
                _circuits.value = circuits
            } catch (e: retrofit2.HttpException) {
                Log.i("Tests", "HTTP error: ${e.code()} - ${e.message()}")
            } catch (e: Exception) {
                Log.i("Tests", "Error fetching circuits: ${e.message.orEmpty()}")
            }
        }
    }

    fun fetchCircuitsBySeason(season: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val circuits = f1Repository.getCircuitsBySeason(season)
                _circuits.value = circuits
            } catch (e: retrofit2.HttpException) {
                Log.i("Tests", "HTTP error: ${e.code()} - ${e.message()}")
            } catch (e: Exception) {
                Log.i("Tests", "Error fetching circuits: ${e.message.orEmpty()}")
            }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as F1TriviaApplication
                MainViewModel(application.f1Repository)
            }
        }
    }

}