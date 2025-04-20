package com.vozmediano.f1trivia.domain

import com.vozmediano.f1trivia.domain.model.f1.Race

interface F1RaceRepository {
    suspend fun getRacesByCircuitAndPosition(circuitId: String, position: String) : List<Race>
    suspend fun getRaceBySeasonAndRound(season: String, round: String) : Race
}