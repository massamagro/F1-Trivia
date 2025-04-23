package com.vozmediano.f1trivia.data

import android.util.Log
import com.vozmediano.f1trivia.data.local.dao.CircuitDao
import com.vozmediano.f1trivia.data.mappers.toDatabase
import com.vozmediano.f1trivia.data.mappers.toDomain
import com.vozmediano.f1trivia.data.network.api.F1Service
import com.vozmediano.f1trivia.domain.F1CircuitRepository
import com.vozmediano.f1trivia.domain.model.f1.Circuit

class F1CircuitRepositoryImpl(
    private val f1Service: F1Service,
    private val circuitDao: CircuitDao
) : F1CircuitRepository {
    override suspend fun getCircuits(): List<Circuit> {
        var circuits = mutableListOf<Circuit>()
        try {
            circuitDao.getCircuits()
                .map { it.toDomain() }
                .also {
                    circuits = it.toMutableList()
                    Log.i("F1CircuitRepositoryImpl", "${circuits.size} circuits found in database")
                }
            if (circuits.size > 0) {
                Log.i("F1CircuitRepositoryImpl", "returning ${circuits.size} circuits")
                return circuits
            } else {
                throw Exception("No circuits found in database")
            }
        } catch (e: Exception) {
            Log.i("F1CircuitRepositoryImpl", "No circuits found in database, fetching from API")
            Log.i("F1CircuitRepositoryImpl", "Error: ${e.message}")
            var offset = 0
            val limit = 100

            while (true) {
                try {
                    val response = f1Service.getCircuits(limit, offset)
                    val circuitDtos = response.mrData.circuitTable!!.circuitDtos!!
                    circuits.addAll(circuitDtos.map { it.toDomain() })
                    circuitDao.upsertAll(circuitDtos.map { it.toDomain().toDatabase() })
                    offset += limit
                    val totalCircuits = response.mrData.total.toIntOrNull() ?: Int.MAX_VALUE
                    if (offset >= totalCircuits) {
                        break
                    }

                } catch (e: Exception) {
                    Log.i("F1CircuitRepositoryImpl", "Error fetching from API: ${e.message}")
                    break
                }
            }
        }
        return circuits
    }

    override suspend fun getCircuitsBySeason(season: String): List<Circuit> {
        val response = f1Service.getCircuitsBySeason(season)
        val circuitDtos = response.mrData.circuitTable?.circuitDtos
        if (circuitDtos != null) {
            circuitDao.upsertAll(circuitDtos.map { it.toDomain().toDatabase() })
            return circuitDtos.map { it.toDomain() }
        } else {
            return emptyList()
        }
    }

    override suspend fun clearAllData() {
        try {
            circuitDao.clearAll()
            Log.i("F1CircuitRepositoryImpl", "All circuits cleared from database")
        } catch (e: Exception) {
            Log.i("F1CircuitRepositoryImpl", "Error clearing circuits from database: ${e.message}")
        }
    }
}