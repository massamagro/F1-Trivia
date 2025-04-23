package com.vozmediano.f1trivia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vozmediano.f1trivia.domain.F1CircuitRepository
import com.vozmediano.f1trivia.domain.F1ConstructorRepository
import com.vozmediano.f1trivia.domain.F1DriverRepository
import com.vozmediano.f1trivia.domain.F1RaceRepository
import com.vozmediano.f1trivia.domain.F1ResultRepository
import com.vozmediano.f1trivia.domain.WikiRepository
import com.vozmediano.f1trivia.domain.model.usecase.DriverByNationalityUseCase
import com.vozmediano.f1trivia.domain.model.usecase.MostPodiumsByCircuitUseCase
import com.vozmediano.f1trivia.domain.model.usecase.MostWinsByCircuitUseCase
import com.vozmediano.f1trivia.domain.model.usecase.WhoWonAtCircuitAndSeasonUseCase
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val f1CircuitRepository: F1CircuitRepository,
    private val f1ConstructorRepository: F1ConstructorRepository,
    private val f1DriverRepository: F1DriverRepository,
    private val f1RaceRepository: F1RaceRepository,
    private val f1ResultRepository: F1ResultRepository,
    private val wikiRepository: WikiRepository
): ViewModel() {

    fun refreshData(){
        viewModelScope.launch {
            f1CircuitRepository.clearAllData()
            f1ConstructorRepository.clearAllData()
            f1DriverRepository.clearAllData()
            f1RaceRepository.clearAllData()
            f1ResultRepository.clearAllData()
            wikiRepository.clearAllData()
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as F1TriviaApplication
                SettingsViewModel(
                    application.f1CircuitRepository,
                    application.f1ConstructorRepository,
                    application.f1DriverRepository,
                    application.f1RaceRepository,
                    application.f1ResultRepository,
                    application.wikiRepository
                )
            }
        }
    }
}