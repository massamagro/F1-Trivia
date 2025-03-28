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
            // Fetch from API first
            val driverResponse = f1Service.getDriver(driverId)
            val mrData = driverResponse.mrData
            Log.i("Tests", "(RE) MRData: $mrData")
            val driverTable = mrData.driverTable
            Log.i("Tests", "(RE) DriverTable: $driverTable")
            val drivers = driverTable.driverDtos
            Log.i("Tests", "(RE) Drivers: $drivers")
            val driver = drivers.first()
            Log.i("Tests", "(RE) driver: $driver")
            val dr = driver.toDomain()

            // Save in local database
            //driverDao.insertAll(listOf(driverResponse.toDatabase()))

            Log.i("Tests", "(RE) Loaded from request: $driverResponse")
            dr
        } catch (e: Exception) {
            Log.e("Tests", "(RE) API request failed: ${e.message}", e)

            // If API fails, load from cache
            val cachedDriver = driverDao.getDriver(driverId)?.toDomain()
            if (cachedDriver != null) {
                Log.i("Tests", "(RE) Loaded from cache as fallback: $cachedDriver")
                return cachedDriver
            }

            // If cache is also empty, throw an exception or return a default Driver
            throw Exception("No driver data available for ID: $driverId")
        }
    }


}