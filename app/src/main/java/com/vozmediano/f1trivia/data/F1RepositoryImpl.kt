package com.vozmediano.f1trivia.data

import com.vozmediano.f1trivia.data.local.dao.DriverDao
import com.vozmediano.f1trivia.data.network.api.F1Service

import com.vozmediano.f1trivia.domain.F1Repository
import com.vozmediano.f1trivia.domain.model.Driver

class F1RepositoryImpl(
    private val f1Service: F1Service,
    private val driverDao: DriverDao

) : F1Repository {
    override suspend fun getDrivers(driverId: String): List<Driver> {
        TODO("Not yet implemented")
    }

    override suspend fun getDriver(driverId: String): Driver {
        return try{
            val cachedDriver = driverDao.getDriver(driverId).toDomain()
            cachedDriver
        } catch (e: Exception){
            val driverResponse =
                f1Service
                    .getDriver(driverId)
                    .DriverTable
                    .Drivers
                    .first()
                    .toDomain()
            driverDao.insertAll(listOf(driverResponse.toDatabase()))
            driverResponse
        }
    }


}