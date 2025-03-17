package com.vozmediano.f1trivia.data.network.api

import com.vozmediano.f1trivia.data.network.model.DriversResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface F1Service {
    @GET("drivers")
    suspend fun getDriver(
        @Query("driver_number") driverNumber: Int
    ): List<DriversResponse>
}