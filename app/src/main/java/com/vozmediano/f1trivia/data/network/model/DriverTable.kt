package com.vozmediano.f1trivia.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DriverTable(
    @Json(name = "driverId") val driverId: String ? = null,
    @Json(name = "Drivers") val driverDtos: List<DriverDto> ? = null,
    @Json(name = "season") val season: String ? = null
)
