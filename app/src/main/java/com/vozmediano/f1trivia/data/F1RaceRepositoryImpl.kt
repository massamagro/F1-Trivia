package com.vozmediano.f1trivia.data

import android.util.Log
import com.vozmediano.f1trivia.data.local.dao.RaceDao
import com.vozmediano.f1trivia.data.mappers.toDatabase
import com.vozmediano.f1trivia.data.mappers.toDomain
import com.vozmediano.f1trivia.data.network.api.F1Service
import com.vozmediano.f1trivia.domain.F1RaceRepository
import com.vozmediano.f1trivia.domain.model.f1.Race

class F1RaceRepositoryImpl(
    private val f1service: F1Service,
    private val raceDao: RaceDao
) : F1RaceRepository {

    override suspend fun getRacesByCircuitAndPosition(
        circuitId: String,
        position: String
    ): List<Race> {
        return try {
            val limit = 100
            val races = mutableListOf<Race>()
            val response = f1service.getRacesByCircuitAndPosition(circuitId, position, limit)
            val raceDtos = response.mrData.raceTable!!.racesDto!!
            races.addAll(raceDtos.map { it.toDomain() })
            raceDao.upsertAll(raceDtos.map { it.toDomain().toDatabase() })
            races
        } catch (e: Exception) {
            Log.i("F1RaceRepositoryImpl", e.toString())
            emptyList()
        }
    }

    override suspend fun getRaceBySeasonAndRound(season: String, round: String): Race {
        return try {
            val response = f1service.getRaceBySeasonAndCircuit(season, round)
            val race = response.mrData.raceTable!!.racesDto!!.first().toDomain()
            race
        } catch (e: Exception) {
            Log.i("F1RaceRepositoryImpl", e.toString())
            throw e
        }
    }

    override suspend fun clearAllData() {
        try {
            raceDao.clearAll()
            Log.i("F1RaceRepositoryImpl", "All results cleared from database")

        } catch (e: Exception) {
            Log.i("F1RaceRepositoryImpl", e.toString())
        }
    }


}