package com.vozmediano.f1trivia

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vozmediano.f1trivia.domain.F1Repository
import com.vozmediano.f1trivia.domain.model.Driver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(val f1Repository: F1Repository) : ViewModel() {

    private val _driver = MutableStateFlow<Driver?>(null)
    val driver: StateFlow<Driver?> = _driver.asStateFlow()

    fun fetchDriver(driverId: String) {
        viewModelScope.launch {
            try {
                val driver = f1Repository.getDriver(driverId)
                _driver.value = driver
                Log.i("Tests", "Driver: $driver")
            } catch (e: Exception) {
                Log.i("Tests", "error fetching")
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