package com.vozmediano.f1trivia.data.network.model.result

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.vozmediano.f1trivia.data.network.model.constructor.ConstructorDto
import com.vozmediano.f1trivia.data.network.model.driver.DriverDto
import com.vozmediano.f1trivia.data.network.model.fastestlap.FastestLapDto
import com.vozmediano.f1trivia.data.network.model.time.TimeDto

@JsonClass(generateAdapter = true)
data class ResultDto(
    @Json(name = "number") val number: String,
    @Json(name = "position") val position: String,
    @Json(name = "positionText") val positionText: String,
    @Json(name = "points") val points: String,
    @Json(name = "Driver") val driverDto: DriverDto,
    @Json(name = "Constructor") val constructorDto: ConstructorDto,
    @Json(name = "grid") val grid: String,
    @Json(name = "laps") val laps: String,
    @Json(name = "status") val status: String,
    @Json(name = "Time") val timeDto: TimeDto? = null,
    @Json(name = "FastestLap") val fastestLapDto: FastestLapDto ? = null
)