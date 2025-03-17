package com.vozmediano.f1trivia

import android.app.Application
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.vozmediano.f1trivia.data.network.api.F1Service
import com.vozmediano.f1trivia.domain.F1Repository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class F1TriviaApplication : Application() {
    lateinit var F1Repository: F1Repository

    private fun getF1Service(): F1Service {
        val client = OkHttpClient.Builder()
            .build()

        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}