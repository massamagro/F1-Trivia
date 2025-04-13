package com.vozmediano.f1trivia.data.network.model.fastestlap

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.vozmediano.f1trivia.data.network.model.averagespeed.AverageSpeedDto
import com.vozmediano.f1trivia.data.network.model.time.TimeDto

@JsonClass(generateAdapter = true)
data class FastestLapDto(
    @Json(name = "rank") val rank: String,
    @Json(name = "lap") val lap: String,
    @Json(name = "Time") val time: TimeDto,
    @Json(name = "AverageSpeed") val averageSpeed: AverageSpeedDto
)
