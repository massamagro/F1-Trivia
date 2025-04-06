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
            val correctSeason = (2023..2024).random().toString()
            val circuits = try {
                f1Repository.getCircuitsBySeason(correctSeason)
            } catch (e: Exception) {
                Log.e("ViewModel", "Failed to fetch circuits: ${e.message}")
                _question.value = null
                return@launch
            }
            if (circuits.isEmpty()) {
                Log.e("ViewModel", "No circuits found for year $correctSeason")
                _question.value = null
                return@launch
            }
            val correctCircuit = circuits.random().circuitId
            val correctPostion = "1"

            // Prepare the question object
            val question = Question(
                "Who won the ${
                    correctCircuit.replaceFirstChar { it.uppercase() }.replace('_', ' ')
                } GP in $correctSeason?",
                mutableListOf()
            )

            try {
                // Fetch correct driver for the circuit and year
                val correctDriver = try {
                    f1Repository.getDriverBySeasonAndCircuitAndPosition(
                        correctSeason,
                        correctCircuit,
                        correctPostion
                    )
                } catch (e: Exception) {
                    Log.e("ViewModel", "Correct driver fetch failed: ${e.message}")
                    _question.value = null
                    return@launch
                }
                val setOptions = mutableListOf<Option>()
                val setDrivers = mutableSetOf<String>() // To avoid duplicates


                setDrivers.add(correctDriver.driverId)
                question.options.add(
                    Option(
                        id = 0,
                        shortText = "${correctDriver.givenName} ${correctDriver.familyName}",
                        longText = "${correctDriver.givenName} ${correctDriver.familyName} won the $correctCircuit GP in $correctSeason",
                        isCorrect = true
                    )
                )

                // Fetch 3 wrong drivers as distractors from different years and circuits
                while (setDrivers.size < 4) {
                    var season = ""
                    var circuitId = ""
                    var position = ""
                    when ((1..5).random()) {
                        //Same circuit, different season
                        1 -> {
                            Log.i("ViewModel", "Same circuit, different season")
                            season = correctSeason + (-2..2).random()
                            circuitId = correctCircuit
                            position = "1"
                        }
                        //Different circuit, same season
                        2 -> {
                            Log.i("ViewModel", "Different circuit, same season")
                            season = correctSeason
                            circuitId = circuits.filter { it.circuitId != correctCircuit }
                                .random().circuitId
                            position = "1"
                        }
                        //Same circuit, same season, 2nd position
                        3 -> {
                            Log.i("ViewModel", "Same circuit, same season, 2nd position")
                            season = correctSeason
                            circuitId = correctCircuit
                            position = "2"
                        }
                        //Same circuit, same season, 3rd position
                        4 -> {
                            Log.i("ViewModel", "Same circuit, same season, 3rd position")
                            season = correctSeason
                            circuitId = correctCircuit
                            position = "3"
                        }
                        //Different race, same year, podium position
                        5 -> {
                            Log.i("ViewModel", "Different race, same year, podium position")
                            season = correctSeason
                            circuitId = circuits.filter { it.circuitId != correctCircuit }
                                .random().circuitId
                            position = (2..3).random().toString()
                        }


                    }

                    try {
                        val driver = f1Repository.getDriverBySeasonAndCircuitAndPosition(
                            season,
                            circuitId,
                            position
                        )
                        if (driver.driverId == correctDriver.driverId || setDrivers.contains(driver.driverId)) {
                            continue // Skip if it's the same driver
                        }
                        setDrivers.add(driver.driverId)
                        setOptions.add(
                            Option(
                                id = setOptions.size,
                                shortText = "${driver.givenName} ${driver.familyName}",
                                longText = "",
                                isCorrect = false
                            )
                        )
                    } catch (e: Exception) {
                        Log.i("ViewModel", "Failed to fetch driver for distractor: ${e.message}")
                    }
                }

                question.options.addAll(setOptions)
                question.options.shuffle()
                _question.value = question

            } catch (e: Exception) {
                Log.e("ViewModel", "Unexpected error while generating question: ${e.message}")
                _question.value = null
            }
        }
    }

    fun fetchQuestionDriverByNationality() {
        viewModelScope.launch {
            val drivers = try {
                f1Repository.getDrivers()
            } catch (e: Exception) {
                Log.e("ViewModel", "Failed to fetch drivers: ${e.message}")
                _question.value = null
                return@launch
            }
            val correctDriver = drivers.random()
            val question = Question(
                title = "Which of this drivers is ${correctDriver.nationality}?",
                options = mutableListOf()
            )
            val setNationalities = mutableSetOf<String>() // To avoid duplicates
            setNationalities.add(correctDriver.nationality)
            question.options.add(
                Option(
                    id = 0,
                    shortText = "${correctDriver.givenName} ${correctDriver.familyName}",
                    longText = "${correctDriver.givenName} ${correctDriver.familyName} is ${correctDriver.nationality}",
                    isCorrect = true
                )
            )
            while(question.options.size < 4){
                val driver = drivers.filter {it.nationality != correctDriver.nationality}.random()
                if (setNationalities.contains(driver.nationality)) continue
                setNationalities.add(driver.nationality)
                question.options.add(
                    Option(
                        id = question.options.size,
                        shortText = "${driver.givenName} ${driver.familyName}",
                        longText = "",
                        isCorrect = false
                    )
                )
            }
            question.options.shuffle()
            _question.value = question

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