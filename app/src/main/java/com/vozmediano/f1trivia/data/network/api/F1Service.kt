package com.vozmediano.f1trivia.data.network.api

import com.vozmediano.f1trivia.data.network.model.MRDataResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface F1Service {
    @GET("drivers/{driverId}")
    suspend fun getDriver(
        @Path("driverId") driverId: String
    ): MRDataResponse

    @GET("{season}/drivers")
    suspend fun getDriversBySeason(
        @Path("season") season: String
    ): MRDataResponse
}