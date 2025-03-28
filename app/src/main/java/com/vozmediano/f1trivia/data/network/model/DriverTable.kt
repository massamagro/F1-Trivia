package com.vozmediano.f1trivia.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DriverTable(
    @Json(name = "driverId") val driverId: String,
    @Json(name = "Drivers") val driverDtos: List<DriverDto>
)
