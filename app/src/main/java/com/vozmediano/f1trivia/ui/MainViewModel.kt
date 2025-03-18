package com.vozmediano.f1trivia.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vozmediano.f1trivia.F1TriviaApplication
import com.vozmediano.f1trivia.domain.F1Repository
import com.vozmediano.f1trivia.domain.model.Driver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(val f1Repository: F1Repository) : ViewModel() {

    sealed class DriverUiState {
        object Loading : DriverUiState()
        data class Success(val driver: Driver) : DriverUiState()
        data class Error(val exception: Exception) : DriverUiState()
    }

    private val _uiState = MutableStateFlow<DriverUiState>(DriverUiState.Loading)
    val uiState: StateFlow<DriverUiState> = _uiState.asStateFlow()

    init {
        fetchDriver("alonso")
    }

    private fun fetchDriver(driverId: String) {
        viewModelScope.launch {
            try {
                val driver = f1Repository.getDriver(driverId)
                _uiState.value = DriverUiState.Success(driver)
                Log.i("Tests", "Driver: $driver")
            } catch (e: Exception) {
                _uiState.value = DriverUiState.Error(e)
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