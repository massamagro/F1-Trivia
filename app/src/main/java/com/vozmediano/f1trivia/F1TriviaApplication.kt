package com.vozmediano.f1trivia

import android.app.Application
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.vozmediano.f1trivia.data.F1CircuitRepositoryImpl
import com.vozmediano.f1trivia.data.F1ConstructorRepositoryImpl
import com.vozmediano.f1trivia.data.F1DriverRepositoryImpl
import com.vozmediano.f1trivia.data.local.F1Database
import com.vozmediano.f1trivia.data.network.api.F1Service
import com.vozmediano.f1trivia.domain.F1DriverRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class F1TriviaApplication : Application() {
    lateinit var f1DriverRepository: F1DriverRepository
    lateinit var f1ConstructorRepository: F1ConstructorRepositoryImpl
    lateinit var f1CircuitRepository: F1CircuitRepositoryImpl

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

    override fun onCreate() {
        super.onCreate()
        val service = getF1Service()
        val database = Room.databaseBuilder(
            applicationContext,
            F1Database::class.java, "f1-database"
        ).build()
        val driverDao = database.driverDao()
        val constructorDao = database.constructorDao()
        val circuitDao = database.circuitDao()

        f1DriverRepository = F1DriverRepositoryImpl(service, driverDao)
        f1ConstructorRepository = F1ConstructorRepositoryImpl(service, constructorDao)
        f1CircuitRepository = F1CircuitRepositoryImpl(service, circuitDao)


    }
}

