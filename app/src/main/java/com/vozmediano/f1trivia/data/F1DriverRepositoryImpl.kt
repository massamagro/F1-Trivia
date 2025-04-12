package com.vozmediano.f1trivia.data

import android.util.Log
import com.vozmediano.f1trivia.data.local.dao.CircuitDao
import com.vozmediano.f1trivia.data.local.dao.ConstructorDao
import com.vozmediano.f1trivia.data.local.dao.DriverDao
import com.vozmediano.f1trivia.data.mappers.toDatabase
import com.vozmediano.f1trivia.data.mappers.toDomain
import com.vozmediano.f1trivia.data.network.api.F1Service

import com.vozmediano.f1trivia.domain.F1DriverRepository
import com.vozmediano.f1trivia.domain.model.f1.Circuit
import com.vozmediano.f1trivia.domain.model.f1.Driver
import com.vozmediano.f1trivia.domain.model.f1.Constructor


class F1DriverRepositoryImpl(
    private val f1Service: F1Service,
    private val driverDao: DriverDao,
    private val circuitDao: CircuitDao

) : F1DriverRepository {
    override suspend fun getDrivers(): List<Driver> {
        var drivers = mutableListOf<Driver>()
        return try{
            drivers = driverDao.getDrivers().map { it.toDomain() }.toMutableList()
            drivers
        } catch (e: Exception) {
            Log.i("Tests", "Error getting drivers from database: ${e.message}")
            var offset = 0
            val limit = 100

            drivers = mutableListOf<Driver>()
            while (true) {
                try {
                    val response = f1Service.getDrivers(limit, offset)
                    val driverDtos = response.mrData.driverTable!!.driverDtos!!
                    drivers.addAll(driverDtos.map { it.toDomain() })
                    driverDao.upsertAll(driverDtos.map { it.toDomain().toDatabase() })
                    offset += limit
                    val totalDrivers = response.mrData.total?.toIntOrNull() ?: Int.MAX_VALUE
                    if (offset >= totalDrivers) {
                        break
                    }
                } catch (e: Exception) {
                    break
                }
            }
            drivers
        }


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
    override suspend fun getDriverBySeasonAndCircuitAndPosition(season: String, circuit: String, position: String): Driver {

            val response = f1Service.getDriverBySeasonAndCircuitAndPosition(season, circuit, position)
            Log.i("Tests", "Response: $response")
            val driver = response.mrData.raceTable?.racesDto
                ?.firstOrNull()
                ?.results
                ?.firstOrNull()
                ?.driverDto
                ?.toDomain()
            return driver
                ?: throw Exception("Driver not found")
    }







}