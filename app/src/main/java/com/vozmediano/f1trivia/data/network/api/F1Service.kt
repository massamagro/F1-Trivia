package com.vozmediano.f1trivia.data.network.api

import com.vozmediano.f1trivia.data.network.model.MRDataResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface F1Service {

    //DRIVERS
    @GET("drivers")
    suspend fun getDrivers(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): MRDataResponse

    @GET("drivers/{driverId}")
    suspend fun getDriverById(
        @Path("driverId") driverId: String
    ): MRDataResponse

    @GET("{season}/drivers")
    suspend fun getDriversBySeason(
        @Path("season") season: String
    ): MRDataResponse

    @GET("{season}/circuits/{circuit}/results/{position}")
    suspend fun getDriverBySeasonAndCircuitAndPosition(
        @Path("season") season: String,
        @Path("circuit") circuit: String,
        @Path("position") position: String
    ): MRDataResponse


    //CONSTRUCTORS
    @GET("constructors")
    suspend fun getConstructors(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): MRDataResponse

    @GET("constructors/{constructorId}")
    suspend fun getConstructorById(
        @Path("constructorId") constructorId: String
    ): MRDataResponse

    @GET("{season}/constructors")
    suspend fun getConstructorsBySeason(
        @Path("season") season: String
    ): MRDataResponse

    //CIRCUITS
    @GET("circuits")
    suspend fun getCircuits(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): MRDataResponse

    @GET("{season}/circuits")
    suspend fun getCircuitsBySeason(
        @Path("season") season: String
    ): MRDataResponse

    //RACES
    @GET("circuits/{circuitId}/results/{position}/")
    suspend fun getRacesByCircuitAndPosition(
        @Path("circuitId") circuitId: String,
        @Path("position") position: String,
        @Query("limit") limit: Int
    ): MRDataResponse

    @GET("{season}/circuits/{circuitId}/results/")
    suspend fun getRaceBySeasonAndCircuit(
        @Path("season") season: String,
        @Path("circuitId") circuitId: String
    ): MRDataResponse

    //RESULTS
    @GET("{season}/results/")
    suspend fun getResultsBySeason(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Path("season") season: String
    ): MRDataResponse

    @GET("{season}/{round}/results/")
    suspend fun getResultsBySeasonAndRound(
        @Path("season") season: String,
        @Path("round") round: String
    ): MRDataResponse
}