package com.vozmediano.f1trivia

import android.app.Application
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.vozmediano.f1trivia.data.F1CircuitRepositoryImpl
import com.vozmediano.f1trivia.data.F1ConstructorRepositoryImpl
import com.vozmediano.f1trivia.data.F1DriverRepositoryImpl
import com.vozmediano.f1trivia.data.F1RaceRepositoryImpl
import com.vozmediano.f1trivia.data.F1ResultRepositoryImpl
import com.vozmediano.f1trivia.data.WikiRepositoryImpl
import com.vozmediano.f1trivia.data.local.F1Database
import com.vozmediano.f1trivia.data.local.WikiDatabase
import com.vozmediano.f1trivia.data.network.api.F1Service
import com.vozmediano.f1trivia.data.network.api.WikiService
import com.vozmediano.f1trivia.domain.F1DriverRepository
import com.vozmediano.f1trivia.domain.WikiRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class F1TriviaApplication : Application() {
    lateinit var f1DriverRepository: F1DriverRepository
    lateinit var f1ConstructorRepository: F1ConstructorRepositoryImpl
    lateinit var f1CircuitRepository: F1CircuitRepositoryImpl
    lateinit var f1RaceRepository: F1RaceRepositoryImpl
    lateinit var f1ResultRepository: F1ResultRepositoryImpl
    lateinit var wikiRepository: WikiRepository

    private fun getF1Service(): F1Service {
        val client = OkHttpClient.Builder()
            .build()

        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.jolpi.ca/ergast/f1/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(F1Service::class.java)
    }

    private fun getWikiService(): WikiService {
        val client = OkHttpClient.Builder()
            .build()

        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://en.wikipedia.org/api/rest_v1/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(WikiService::class.java)
    }

    override fun onCreate() {
        super.onCreate()
        val f1Service = getF1Service()
        val wikiService = getWikiService()
        val f1Database = Room.databaseBuilder(
            applicationContext,
            F1Database::class.java, "f1-database"
        ).build()
        val wikiDatabase = Room.databaseBuilder(
            applicationContext,
            WikiDatabase::class.java, "wiki-database"
        ).build()
        val driverDao = f1Database.driverDao()
        val constructorDao = f1Database.constructorDao()
        val circuitDao = f1Database.circuitDao()
        val resultDao = f1Database.resultDao()
        val raceDao = f1Database.raceDao()
        val imageDao = wikiDatabase.imageDao()

        f1DriverRepository = F1DriverRepositoryImpl(f1Service, driverDao)
        f1ConstructorRepository = F1ConstructorRepositoryImpl(f1Service, constructorDao)
        f1CircuitRepository = F1CircuitRepositoryImpl(f1Service, circuitDao)
        f1ResultRepository = F1ResultRepositoryImpl(f1Service, resultDao)
        f1RaceRepository = F1RaceRepositoryImpl(f1Service, raceDao)
        wikiRepository = WikiRepositoryImpl(wikiService, imageDao)



    }
}

