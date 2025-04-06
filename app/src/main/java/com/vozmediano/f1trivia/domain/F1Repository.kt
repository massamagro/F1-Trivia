package com.vozmediano.f1trivia.domain

import com.vozmediano.f1trivia.domain.model.f1.Circuit
import com.vozmediano.f1trivia.domain.model.f1.Constructor
import com.vozmediano.f1trivia.domain.model.f1.Driver


interface F1Repository {
    //DRIVERS
    suspend fun getDrivers(): List<Driver>
    suspend fun getDriverById(driverId: String): Driver
    suspend fun getDriversBySeason(season: String): List<Driver>
    suspend fun getDriverBySeasonAndCircuitAndPosition(
        season: String,
        circuit: String,
        position: String
    ): Driver


    //CONSTRUCTORS
    suspend fun getConstructors(): List<Constructor>
    suspend fun getConstructorById(constructorId: String): Constructor
    suspend fun getConstructorsBySeason(season: String): List<Constructor>


    //CIRCUITS
    suspend fun getCircuits(): List<Circuit>
    suspend fun getCircuitsBySeason(season: String): List<Circuit>

}