package com.vozmediano.f1trivia.data

import android.util.Log
import com.vozmediano.f1trivia.data.local.dao.ConstructorDao
import com.vozmediano.f1trivia.data.local.dao.DriverDao
import com.vozmediano.f1trivia.data.mappers.toDatabase
import com.vozmediano.f1trivia.data.mappers.toDomain
import com.vozmediano.f1trivia.data.network.api.F1Service

import com.vozmediano.f1trivia.domain.F1Repository
import com.vozmediano.f1trivia.domain.model.Driver
import com.vozmediano.f1trivia.domain.model.Constructor


class F1RepositoryImpl(
    private val f1Service: F1Service,
    private val driverDao: DriverDao,
    private val constructorDao: ConstructorDao

) : F1Repository {
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
            } catch (e: Exception) {
                break
            }
        }
        return drivers

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


}