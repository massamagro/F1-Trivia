package com.vozmediano.f1trivia.data.network.api

import com.vozmediano.f1trivia.data.network.model.DriversResponse
import com.vozmediano.f1trivia.data.network.model.MRDataResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface F1Service {
    @GET("/drivers")
    suspend fun getDriver(
        @Query("driverId") driverId: String
    ): MRDataResponse

    @GET("/2024/drivers")
    suspend fun getDrivers(
    ): MRDataResponse
}