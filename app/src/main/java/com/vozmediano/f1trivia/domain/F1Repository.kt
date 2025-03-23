package com.vozmediano.f1trivia.domain

import com.vozmediano.f1trivia.domain.model.Driver


interface F1Repository {
    suspend fun getDrivers(): List<Driver>
    suspend fun getDriver(driverId: String): Driver
}