package com.vozmediano.f1trivia.data.network.model.race

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RaceTable(
    @Json(name = "position") val position: String ? = null,
    @Json(name = "season") val season: String ? = null,
    @Json(name = "circuitId") val circuitId: String ? = null,
    @Json(name = "Races") val racesDto: List<RaceDto> ? = null
)
