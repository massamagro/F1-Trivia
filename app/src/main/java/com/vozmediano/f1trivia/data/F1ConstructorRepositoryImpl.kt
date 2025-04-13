package com.vozmediano.f1trivia.data

import android.util.Log
import com.vozmediano.f1trivia.data.local.dao.ConstructorDao
import com.vozmediano.f1trivia.data.mappers.toDatabase
import com.vozmediano.f1trivia.data.mappers.toDomain
import com.vozmediano.f1trivia.data.network.api.F1Service
import com.vozmediano.f1trivia.domain.F1ConstructorRepository
import com.vozmediano.f1trivia.domain.model.f1.Constructor

class F1ConstructorRepositoryImpl(
    private val f1Service: F1Service,
    private val constructorDao: ConstructorDao
) : F1ConstructorRepository {

    override suspend fun getConstructors(): List<Constructor> {
        var constructors = mutableListOf<Constructor>()
        try {
            constructorDao.getConstructors()
                .map { it.toDomain() }
                .also {
                    constructors = it.toMutableList()
                    Log.i("F1ConstructorRepositoryImpl", "${constructors.size} drivers found in database")
                }

            if (constructors.size > 0) {
                Log.i("F1ConstructorRepositoryImpl", "returning ${constructors.size} drivers")
                return constructors
            } else {
                throw Exception("No constructors found in database")
            }
        } catch (e: Exception) {
            Log.i("F1ConstructorRepositoryImpl", "No constructors found in database, fetching from API")
            Log.i("F1ConstructorRepositoryImpl", "Error: ${e.message}")
            var offset = 0
            val limit = 100

            while (true) {
                try {
                    val response = f1Service.getConstructors(limit, offset)
                    val constructorDtos = response.mrData.constructorTable!!.constructorDtos!!
                    constructors.addAll(constructorDtos.map { it.toDomain() })
                    constructorDao.upsertAll(constructorDtos.map { it.toDomain().toDatabase() })
                    offset += limit
                    val totalConstructors = response.mrData.total.toIntOrNull() ?: Int.MAX_VALUE
                    if (offset >= totalConstructors) {
                        break
                    }
                } catch (e: Exception) {
                    Log.i("F1ConstructorRepositoryImpl", "Error fetching from API: ${e.message}")
                    break
                }
            }
        }
        return constructors
    }


    override suspend fun getConstructorById(constructorId: String): Constructor {
        return try {
            val constructor = constructorDao.getConstructor(constructorId).toDomain()
            Log.i("F1ConstructorRepositoryImpl", "constructor found in database")
            constructor

        } catch (e: Exception) {
            val constructor = f1Service
                .getConstructorById(constructorId)
                .mrData
                .constructorTable!!
                .constructorDtos!!
                .first()
                .toDomain()
            Log.i("F1ConstructorRepositoryImpl", "constructor fetched from api: $constructor")
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
}