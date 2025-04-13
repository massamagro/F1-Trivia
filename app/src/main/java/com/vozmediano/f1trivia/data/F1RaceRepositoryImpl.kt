package com.vozmediano.f1trivia.data

import com.vozmediano.f1trivia.data.mappers.toDomain
import com.vozmediano.f1trivia.data.network.api.F1Service
import com.vozmediano.f1trivia.domain.F1RaceRepository
import com.vozmediano.f1trivia.domain.model.f1.Race

class F1RaceRepositoryImpl (
    private val f1service : F1Service
) : F1RaceRepository {
    override suspend fun getRacesByCircuitAndPosition(circuitId: String, position: String): List<Race> {
        return try{
            val limit = 100
            val races = mutableListOf<Race>()
            val response = f1service.getRacesByCircuitAndPosition(circuitId, position, limit)
            val raceDtos = response.mrData.raceTable!!.racesDto!!
            races.addAll(raceDtos.map {it.toDomain()})
            races
        }
        catch (e: Exception) {
            emptyList()
        }
    }
}