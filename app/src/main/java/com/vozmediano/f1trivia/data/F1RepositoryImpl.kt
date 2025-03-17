package com.vozmediano.f1trivia.data

import com.vozmediano.f1trivia.data.local.dao.DriverDao
import com.vozmediano.f1trivia.data.network.api.F1Service

import com.vozmediano.f1trivia.domain.F1Repository
import com.vozmediano.f1trivia.domain.model.Driver

class F1RepositoryImpl(
    private val f1Service: F1Service,
    private val driverDao: DriverDao

) : F1Repository {
    override suspend fun getDrivers(driverNumber: Int): List<Driver> {
        TODO("Not yet implemented")
    }

    override suspend fun getDriver(driverNumber: Int): Driver {
        return try{
            val driversResponse = f1Service.getDriver(driverNumber)
            val driver = driversResponse.first().toDomain()
            driverDao.insertAll(driver.toDatabase())
            driver
        } catch (ex: Exception){
            val cachedDriver = driverDao.getDriver(driverNumber).first().toDomain()
            cachedDriver
        }
    }


}