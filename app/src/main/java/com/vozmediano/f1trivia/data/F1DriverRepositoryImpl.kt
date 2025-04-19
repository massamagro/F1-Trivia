package com.vozmediano.f1trivia.data

import android.util.Log
import com.vozmediano.f1trivia.data.local.dao.DriverDao
import com.vozmediano.f1trivia.data.mappers.toDatabase
import com.vozmediano.f1trivia.data.mappers.toDomain
import com.vozmediano.f1trivia.data.network.api.F1Service

import com.vozmediano.f1trivia.domain.F1DriverRepository
import com.vozmediano.f1trivia.domain.model.f1.Driver


class F1DriverRepositoryImpl(
    private val f1Service: F1Service,
    private val driverDao: DriverDao

) : F1DriverRepository {
    override suspend fun getDrivers(): List<Driver> {
        var drivers = mutableListOf<Driver>()
        try {
            driverDao.getDrivers()
                .map { it.toDomain() }
                .also {
                    drivers = it.toMutableList()
                    Log.i("F1DriverRepositoryImpl", "${drivers.size} drivers found in database")
                }
            if (drivers.size > 0) {
                Log.i("F1DriverRepositoryImpl", "returning ${drivers.size} drivers")
                return drivers
            } else {
                Log.i("F1DriverRepositoryImpl", "No drivers found in database")
                throw Exception("No drivers found in database")
            }

        } catch (e: Exception) {
            Log.i("F1DriverRepositoryImpl", "No drivers found in database, fetching from API")
            Log.i("F1DriverRepositoryImpl", "Error: ${e.message}")
            var offset = 0
            val limit = 100

            while (true) {
                try {
                    val response = f1Service.getDrivers(limit, offset)
                    val driverDtos = response.mrData.driverTable!!.driverDtos!!
                    drivers.addAll(driverDtos.map { it.toDomain() })
                    driverDao.upsertAll(driverDtos.map { it.toDomain().toDatabase() })
                    offset += limit
                    val totalDrivers = response.mrData.total.toIntOrNull() ?: Int.MAX_VALUE
                    if (offset >= totalDrivers) {
                        break
                    }
                } catch (e: Exception) {
                    Log.i("F1DriverRepositoryImpl", "Error fetching from API: ${e.message}")
                    break
                }
            }
        }
        return drivers
    }

    override suspend fun getDriverById(driverId: String): Driver {
        return try {
            driverDao.getDriver(driverId).toDomain()
        } catch (e: Exception) {
            val driver =
                f1Service
                    .getDriverById(driverId)
                    .mrData
                    .driverTable!!
                    .driverDtos!!
                    .first()
                    .toDomain()
            driverDao.upsert(driver.toDatabase())
            driver


        }
    }

    override suspend fun getDriversBySeason(season: String): List<Driver> {
        return try {
            val drivers =
                f1Service
                    .getDriversBySeason(season)
                    .mrData
                    .driverTable!!
                    .driverDtos!!
                    .map { it.toDomain() }

            driverDao.upsertAll(drivers.map { it.toDatabase() })
            drivers

        } catch (e: Exception) {
            emptyList()
        }
    }
/*
    override suspend fun getDriverBySeasonAndCircuitAndPosition(
        season: String,
        circuit: String,
        position: String
    ): Driver {

        val response = f1Service.getDriverBySeasonAndCircuitAndPosition(season, circuit, position)
        Log.i("F1DriverRepositoryImpl", "Response: $response")
        val driver = response.mrData.raceTable?.racesDto
            ?.firstOrNull()
            ?.results
            ?.firstOrNull()
            ?.driverDto
            ?.toDomain()
        return driver
            ?: throw Exception("Driver not found")
    }*/
}