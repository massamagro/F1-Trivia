package com.vozmediano.f1trivia.domain

import com.vozmediano.f1trivia.domain.model.f1.Circuit

interface F1CircuitRepository {
    //CIRCUITS
    suspend fun getCircuits(): List<Circuit>
    suspend fun getCircuitsBySeason(season: String): List<Circuit>
    suspend fun clearAllData()
}