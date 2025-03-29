package com.vozmediano.f1trivia.domain

import com.vozmediano.f1trivia.domain.model.Constructor
import com.vozmediano.f1trivia.domain.model.Driver


interface F1Repository {
    //DRIVERS
    suspend fun getDrivers(): List<Driver>
    suspend fun getDriverById(driverId: String): Driver
    suspend fun getDriversBySeason(season: String): List<Driver>



    //CONSTRUCTORS
    suspend fun getConstructors(): List<Constructor>
    suspend fun getConstructorById(constructorId: String): Constructor
    suspend fun getConstructorsBySeason(season: String): List<Constructor>

}