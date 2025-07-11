package com.vozmediano.f1trivia.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vozmediano.f1trivia.F1TriviaApplication
import com.vozmediano.f1trivia.domain.model.quiz.Question
import com.vozmediano.f1trivia.domain.model.usecase.DriverByNationalityUseCase
import com.vozmediano.f1trivia.domain.model.usecase.MostPodiumsByCircuitUseCase
import com.vozmediano.f1trivia.domain.model.usecase.MostWinsByCircuitUseCase
import com.vozmediano.f1trivia.domain.model.usecase.WhoWonAtCircuitAndSeasonUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(
    private val whoWonAtCircuitAndSeasonUseCase: WhoWonAtCircuitAndSeasonUseCase,
    private val driverByNationalityUseCase: DriverByNationalityUseCase,
    private val mostWinsByCircuitUseCase: MostWinsByCircuitUseCase,
    private val mostPodiumsByCircuitUseCase: MostPodiumsByCircuitUseCase
    ) : ViewModel() {

    private val _question = MutableStateFlow<Question?>(null)
    val question: StateFlow<Question?> = _question.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    //QUESTIONS
    fun fetchQuestionDriverBySeasonAndCircuitAndPosition() {
        fetchQuestion { whoWonAtCircuitAndSeasonUseCase() }
    }

    fun fetchQuestionDriverByNationality() {
        fetchQuestion { driverByNationalityUseCase() }
    }

    fun fetchQuestionDriverByWinsAtCircuit() {
        fetchQuestion { mostWinsByCircuitUseCase() }
    }

    fun fetchQuestionDriverByPodiumsAtCircuit() {
        fetchQuestion { mostPodiumsByCircuitUseCase() }
    }

    private fun fetchQuestion(questionFetcher: suspend () -> Question?) {
        Log.i("GameViewModel", "Fetching new question...")
        _isLoading.value = true
        viewModelScope.launch {
            delay(2000) // Simulate a minimum loading time within the ViewModel
            _question.value = questionFetcher()
            _isLoading.value = false
            Log.i("GameViewModel", "Question fetched.")
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as F1TriviaApplication
                GameViewModel(
                    WhoWonAtCircuitAndSeasonUseCase(application.f1ResultRepository, application.f1RaceRepository, application.wikiRepository),
                    DriverByNationalityUseCase(application.f1DriverRepository, application.wikiRepository),
                    MostWinsByCircuitUseCase(application.f1RaceRepository, application.f1CircuitRepository, application.wikiRepository),
                    MostPodiumsByCircuitUseCase(application.f1RaceRepository, application.f1CircuitRepository, application.wikiRepository)
                )
            }
        }
    }

/*
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



//DRIVERS
fun fetchDrivers() {
    viewModelScope.launch(Dispatchers.IO) {
        try {
            val drivers = f1DriverRepository.getDrivers()
            _drivers.value = drivers
            Log.i("MainViewModel", "Drivers: $drivers")
        } catch (e: retrofit2.HttpException) {
            Log.i("MainViewModel", "HTTP error: ${e.code()} - ${e.message()}")
        } catch (e: Exception) {
            Log.i("MainViewModel", "Error fetching drivers: ${e.message.orEmpty()}")
        }
    }
}

fun fetchDriverById(driverId: String) {
    viewModelScope.launch(Dispatchers.IO) {
        try {
            val driver = f1DriverRepository.getDriverById(driverId)
            _driver.value = driver
            Log.i("MainViewModel", "(VM) Driver: $driver")
        } catch (e: retrofit2.HttpException) {
            Log.i("MainViewModel", "(VM) HTTP error: ${e.code()} - ${e.message()}")
        } catch (e: Exception) {
            Log.i("MainViewModel", "(VM) Error fetching driver: ${e.message.orEmpty()}")
        }
    }
}

fun fetchDriversBySeason(season: String) {
    viewModelScope.launch(Dispatchers.IO) {
        try {
            val drivers = f1DriverRepository.getDriversBySeason(season)
            _drivers.value = drivers
            Log.i("MainViewModel", "Drivers: $drivers")
        } catch (e: retrofit2.HttpException) {
            Log.i("MainViewModel", "HTTP error: ${e.code()} - ${e.message()}")
        } catch (e: Exception) {
            Log.i("MainViewModel", "Error fetching drivers: ${e.message.orEmpty()}")
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
                f1DriverRepository.getDriverBySeasonAndCircuitAndPosition(season, circuit, position)
            _driver.value = driver
        } catch (e: Exception) {
            Log.i("MainViewModel", "Error fetching driver: ${e.message.orEmpty()}")
        }
    }
}


//CONSTRUCTORS
fun fetchConstructors() {
    viewModelScope.launch(Dispatchers.IO) {
        try {
            val constructors = f1ConstructorRepository.getConstructors()
            _constructors.value = constructors
            Log.i("MainViewModel", "Constructors: $constructors")
        } catch (e: retrofit2.HttpException) {
            Log.i("MainViewModel", "HTTP error: ${e.code()} - ${e.message()}")
        } catch (e: Exception) {
            Log.i("MainViewModel", "Error fetching constructors: ${e.message.orEmpty()}")
        }
    }

}

fun fetchConstructorById(constructorId: String) {
    viewModelScope.launch(Dispatchers.IO) {
        try {
            val constructor = f1ConstructorRepository.getConstructorById(constructorId)
            _constructor.value = constructor
            Log.i("MainViewModel", "Constructor: $constructor")
        } catch (e: retrofit2.HttpException) {
            Log.i("MainViewModel", "HTTP error: ${e.code()} - ${e.message()}")
        } catch (e: Exception) {
            Log.i("MainViewModel", "Error fetching constructor: ${e.message.orEmpty()}")
        }
    }
}

fun fetchConstructorsBySeason(season: String) {
    viewModelScope.launch(Dispatchers.IO) {
        try {
            val constructors = f1ConstructorRepository.getConstructorsBySeason(season)
            _constructors.value = constructors
            Log.i("MainViewModel", "Constructors: $constructors")
        } catch (e: retrofit2.HttpException) {
            Log.i("MainViewModel", "HTTP error: ${e.code()} - ${e.message()}")
        } catch (e: Exception) {
            Log.i("MainViewModel", "Error fetching constructors: ${e.message.orEmpty()}")
        }
    }
}

//CIRCUITS
fun fetchCircuits() {

    viewModelScope.launch(Dispatchers.IO) {
        try {
            val circuits = f1CircuitRepository.getCircuits()
            _circuits.value = circuits
        } catch (e: retrofit2.HttpException) {
            Log.i("MainViewModel", "HTTP error: ${e.code()} - ${e.message()}")
        } catch (e: Exception) {
            Log.i("MainViewModel", "Error fetching circuits: ${e.message.orEmpty()}")
        }
    }
}

fun fetchCircuitsBySeason(season: String) {
    viewModelScope.launch(Dispatchers.IO) {
        try {
            val circuits = f1CircuitRepository.getCircuitsBySeason(season)
            _circuits.value = circuits
        } catch (e: retrofit2.HttpException) {
            Log.i("MainViewModel", "HTTP error: ${e.code()} - ${e.message()}")
        } catch (e: Exception) {
            Log.i("MainViewModel", "Error fetching circuits: ${e.message.orEmpty()}")
        }
    }
}
*/

}