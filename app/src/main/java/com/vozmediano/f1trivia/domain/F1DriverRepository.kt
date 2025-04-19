package com.vozmediano.f1trivia.domain

import com.vozmediano.f1trivia.domain.model.f1.Driver

interface F1DriverRepository {
    suspend fun getDrivers(): List<Driver>
    suspend fun getDriverById(driverId: String): Driver
    suspend fun getDriversBySeason(season: String): List<Driver>
    /*suspend fun getDriverBySeasonAndCircuitAndPosition(
        season: String,
        circuit: String,
        position: String
    ): Driver*/

}