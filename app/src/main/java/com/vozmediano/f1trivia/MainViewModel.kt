package com.vozmediano.f1trivia

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vozmediano.f1trivia.domain.F1ConstructorRepository
import com.vozmediano.f1trivia.domain.F1DriverRepository
import com.vozmediano.f1trivia.domain.model.f1.Circuit
import com.vozmediano.f1trivia.domain.model.f1.Constructor
import com.vozmediano.f1trivia.domain.model.f1.Driver
import com.vozmediano.f1trivia.domain.model.quiz.Question
import com.vozmediano.f1trivia.domain.model.usecase.DriverByNationalityUseCase
import com.vozmediano.f1trivia.domain.model.usecase.DriverBySeasonAndCircuitAndPositionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    val f1DriverRepository: F1DriverRepository,
    val f1ConstructorRepository: F1ConstructorRepository,
    private val driverBySeasonAndCircuitAndPositionUseCase: DriverBySeasonAndCircuitAndPositionUseCase,
    private val driverByNationalityUseCase: DriverByNationalityUseCase
    ) : ViewModel() {

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
        viewModelScope.launch{
            _question.value = driverBySeasonAndCircuitAndPositionUseCase()
        }
    }

    fun fetchQuestionDriverByNationality(){
        viewModelScope.launch {
            _question.value = driverByNationalityUseCase()
        }
    }


    //DRIVERS
    fun fetchDrivers() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val drivers = f1DriverRepository.getDrivers()
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
                val driver = f1DriverRepository.getDriverById(driverId)
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
                val drivers = f1DriverRepository.getDriversBySeason(season)
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
                    f1DriverRepository.getDriverBySeasonAndCircuitAndPosition(season, circuit, position)
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
                val constructors = f1ConstructorRepository.getConstructors()
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
                val constructor = f1ConstructorRepository.getConstructorById(constructorId)
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
                val constructors = f1ConstructorRepository.getConstructorsBySeason(season)
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
                val circuits = f1DriverRepository.getCircuits()
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
                val circuits = f1DriverRepository.getCircuitsBySeason(season)
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
                MainViewModel(
                    application.f1DriverRepository,
                    application.f1ConstructorRepository,
                    DriverBySeasonAndCircuitAndPositionUseCase(application.f1DriverRepository),
                    DriverByNationalityUseCase(application.f1DriverRepository)
                )
            }
        }
    }

}