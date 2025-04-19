package com.vozmediano.f1trivia.data

import android.util.Log
import com.vozmediano.f1trivia.data.local.dao.ResultDao
import com.vozmediano.f1trivia.data.mappers.toDatabase
import com.vozmediano.f1trivia.data.mappers.toDomain
import com.vozmediano.f1trivia.data.network.api.F1Service
import com.vozmediano.f1trivia.domain.F1ResultRepository
import com.vozmediano.f1trivia.domain.model.f1.Result

class F1ResultRepositoryImpl(
    private val f1Service: F1Service,
    private val resultDao: ResultDao,
) : F1ResultRepository {
    override suspend fun getResultsBySeason(season: String): List<Result> {
        var results = mutableListOf<Result>()
        try {
            resultDao.getResultsBySeasonAndRound(season)
                .map { it.toDomain() }
                .also {
                    results = it.toMutableList()
                    Log.i(
                        "F1ResultRepositoryImpl",
                        "${results.size} results found for $season season in database"
                    )
                }
            if (results.size > 0) {
                Log.i(
                    "F1ResultRepositoryImpl",
                    "returning ${results.size} results for $season season"
                )
                return results
            } else {
                throw Exception("No results found for $season season in database")
            }
        } catch (e: Exception) {
            Log.i(
                "F1ResultRepositoryImpl",
                "No results found in database for season ${season}, fetching from API"
            )
            Log.i("F1ResultRepositoryImpl", "Error: ${e.message}")
            var offset = 0
            val limit = 100

            while (true) {
                try {
                    val response = f1Service.getResultsBySeason(
                        season = season,
                        limit = limit,
                        offset = offset,
                    )
                    val racesDtos = response
                        .mrData
                        .raceTable!!
                        .racesDto!!
                    racesDtos.forEach { raceDto ->
                        val round = raceDto.round
                        val circuit = raceDto.circuitDto
                        val raceName = raceDto.raceName

                        val resultsDto = raceDto.resultsDto!!
                        results.addAll(resultsDto.map { it.toDomain(season,round,circuit,raceName) })
                        resultDao.upsertAll(resultsDto.map { it.toDomain(season,round,circuit,raceName).toDatabase() })
                    }
                    offset += limit
                    val totalResults = response.mrData.total.toIntOrNull() ?: Int.MAX_VALUE
                    if (offset >= totalResults) {
                        break
                    }
                } catch (e: Exception) {
                    Log.i("F1ResultRepositoryImpl", "Error fetching from API: ${e.message}")
                    break
                }
            }
        }
        return results
    }

}