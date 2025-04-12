package com.vozmediano.f1trivia.data

import android.util.Log
import com.vozmediano.f1trivia.data.local.dao.ConstructorDao
import com.vozmediano.f1trivia.data.mappers.toDatabase
import com.vozmediano.f1trivia.data.mappers.toDomain
import com.vozmediano.f1trivia.data.network.api.F1Service
import com.vozmediano.f1trivia.domain.F1ConstructorRepository
import com.vozmediano.f1trivia.domain.model.f1.Constructor

class F1ConstructorRepositoryImpl (
    private val f1Service: F1Service,
    private val constructorDao: ConstructorDao
) : F1ConstructorRepository{

    override suspend fun getConstructors(): List<Constructor> {
        var constructors = mutableListOf<Constructor>()
        return try{
            constructors = constructorDao.getConstructors().map { it.toDomain() }.toMutableList()
            Log.i("Tests", "constructors found in database")
            constructors
        } catch (e: Exception) {
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
                    break
                }
            }
            constructors
        }


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
}