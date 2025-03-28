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
    override suspend fun getDrivers(): List<Driver> {
        return try {
            // Fetch from API first
            val driversResponse = f1Service
                .getDrivers()
                .mrData
                .driverTable
                .driverDtos
                .map { it.toDomain() }

            // Save in local database
            driverDao.clearAll()  // Optional: Clear old data
            //driverDao.insertAll(driversResponse.map { it.toDatabase() })

            Log.i("Tests", "Loaded from request: $driversResponse")
            driversResponse
        } catch (e: Exception) {
            Log.e("Tests", "API request failed: ${e.message}", e)

            // If API fails, load from cache
            val cachedDrivers = driverDao.getDrivers().map { it.toDomain() }
            Log.i("Tests", "Loaded from cache as fallback: $cachedDrivers")
            cachedDrivers
        }
    }


    override suspend fun getDriver(driverId: String): Driver {
        return try {
            driverDao.getDriver(driverId).toDomain()
        } catch (e: Exception) {
                f1Service
                    .getDriver(driverId)
                    .mrData
                    .driverTable
                    .driverDtos
                    .first()
                    .toDomain()

        }
    }


}