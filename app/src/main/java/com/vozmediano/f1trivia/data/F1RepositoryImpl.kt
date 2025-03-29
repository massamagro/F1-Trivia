package com.vozmediano.f1trivia.data

import android.util.Log
import com.vozmediano.f1trivia.data.local.dao.DriverDao
import com.vozmediano.f1trivia.data.network.api.F1Service

import com.vozmediano.f1trivia.domain.F1Repository
import com.vozmediano.f1trivia.domain.model.Driver

class F1RepositoryImpl(
    private val f1Service: F1Service,
    private val driverDao: DriverDao

) : F1Repository {
    override suspend fun getDriversBySeason(season: String): List<Driver> {
        return try {
            driverDao.getDrivers().map { it.toDomain() }

        } catch (e: Exception) {
            val drivers =
                f1Service
                    .getDriversBySeason(season)
                    .mrData
                    .driverTable
                    .driverDtos!!
                    .map { it.toDomain() }

            driverDao.insertAll(drivers.map { it.toDatabase() })
            drivers
        }
    }


    override suspend fun getDriver(driverId: String): Driver {
        return try {
            driverDao.getDriver(driverId).toDomain()
        } catch (e: Exception) {
            val driver =
                f1Service
                    .getDriver(driverId)
                    .mrData
                    .driverTable
                    .driverDtos!!
                    .first()
                    .toDomain()
            driverDao.insert(driver.toDatabase())
            driver


        }
    }


}