package com.vozmediano.f1trivia.data

import android.util.Log
import com.vozmediano.f1trivia.data.local.dao.CircuitDao
import com.vozmediano.f1trivia.data.local.dao.ConstructorDao
import com.vozmediano.f1trivia.data.local.dao.DriverDao
import com.vozmediano.f1trivia.data.mappers.toDatabase
import com.vozmediano.f1trivia.data.mappers.toDomain
import com.vozmediano.f1trivia.data.network.api.F1Service

import com.vozmediano.f1trivia.domain.F1Repository
import com.vozmediano.f1trivia.domain.model.f1.Circuit
import com.vozmediano.f1trivia.domain.model.f1.Driver
import com.vozmediano.f1trivia.domain.model.f1.Constructor


class F1RepositoryImpl(
    private val f1Service: F1Service,
    private val driverDao: DriverDao,
    private val constructorDao: ConstructorDao,
    private val circuitDao: CircuitDao

) : F1Repository {
    //DRIVERS
    override suspend fun getDrivers(): List<Driver> {
        var offset = 0
        val limit = 100

        val drivers = mutableListOf<Driver>()
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
    override suspend fun getBySeasonAndCircuitAndPosition(season: String, circuit: String, position: String): Driver? {
        return try {
            val response = f1Service.getBySeasonAndCircuitAndPosition(season, circuit, position)
            Log.i("Tests", "Response: $response")
            val driver = response.mrData.raceTable?.racesDto
                ?.firstOrNull()
                ?.results
                ?.firstOrNull()
                ?.driverDto
                ?.toDomain()
            driver

        } catch (e: Exception) {
            Log.i("Tests", "Error fetching driver by season, circuit and position: ${e.message.orEmpty()}")
            null
        }
    }


    //CONSTRUCTORS
    override suspend fun getConstructors(): List<Constructor> {
        var offset = 0
        val limit = 100

        val constructors = mutableListOf<Constructor>()
        while (true) {
            try {
                val response = f1Service.getConstructors(limit, offset)
                val constructorDtos = response.mrData.constructorTable!!.constructorDtos!!
                constructors.addAll(constructorDtos.map { it.toDomain() })
                constructorDao.upsertAll(constructorDtos.map { it.toDomain().toDatabase() })
                offset += limit
                val totalConstructors = response.mrData.total?.toIntOrNull() ?: Int.MAX_VALUE
                if (offset >= totalConstructors) {
                    break
                }
            } catch (e: Exception) {
                break
            }
        }
        return constructors
    }
    override suspend fun getConstructorById(constructorId: String): Constructor {
        return try {
            val constructor = constructorDao.getConstructor(constructorId).toDomain()
            Log.i("Tests", "constructor found in database")
            constructor

        } catch (e: Exception) {
           val constructor = f1Service
                .getConstructorById(constructorId)
                .mrData
                .constructorTable!!
                .constructorDtos!!
                .first()
                .toDomain()
        Log.i("Tests", "constructor fetched from api: $constructor")
        constructorDao.upsert(constructor.toDatabase())
        constructor
    }
}
    override suspend fun getConstructorsBySeason(season: String): List<Constructor> {
        return try {
            val constructors =
                f1Service
                    .getConstructorsBySeason(season)
                    .mrData
                    .constructorTable!!
                    .constructorDtos!!
                    .map { it.toDomain() }

            constructorDao.upsertAll(constructors.map { it.toDatabase() })
            constructors

        } catch (e: Exception) {
            emptyList()
        }
    }

    //CIRCUITS
    override suspend fun getCircuits(): List<Circuit> {
        var offset = 0
        val limit = 100

        val circuits = mutableListOf<Circuit>()
        while (true) {
            try {
                val response = f1Service.getCircuits(limit, offset)
                val circuitDtos = response.mrData.circuitTable!!.circuitDtos!!
                circuits.addAll(circuitDtos.map { it.toDomain() })
                circuitDao.upsertAll(circuitDtos.map { it.toDomain().toDatabase() })
                offset += limit
                val totalCircuits = response.mrData.total?.toIntOrNull() ?: Int.MAX_VALUE
                if (offset >= totalCircuits) {
                    break
                }

            } catch (e: Exception) {
                break
            }
        }
        return circuits
    }


}