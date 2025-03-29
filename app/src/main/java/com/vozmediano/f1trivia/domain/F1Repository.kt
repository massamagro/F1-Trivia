package com.vozmediano.f1trivia.domain

import com.vozmediano.f1trivia.domain.model.Driver


interface F1Repository {
    suspend fun getDriversBySeason(season: String): List<Driver>
    suspend fun getDriver(driverId: String): Driver
}