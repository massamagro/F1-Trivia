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
    fun fetchDriverBySeasonAndCircuitAndPosition(season: String, circuit: String, position: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val driver = f1Repository.getBySeasonAndCircuitAndPosition(season, circuit, position)
                _driver.value = driver
                Log.i("Tests", "(ViewModel) Driver: $driver")
            } catch (e: retrofit2.HttpException) {
                Log.i("Tests", "HTTP error: ${e.code()} - ${e.message()}")
            } catch (e: Exception) {
                Log.i("Tests", "Error fetching driver: ${e.message.orEmpty()}")
            }
        }
    }


    //CONSTRUCTORS
    fun fetchConstructors(){
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

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as F1TriviaApplication
                MainViewModel(application.f1Repository)
            }
        }
    }

}