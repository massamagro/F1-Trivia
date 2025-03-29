package com.vozmediano.f1trivia.data.network.model.circuit

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CircuitDto(
    @Json(name = "circuitId") val circuitId: String,
    @Json(name = "url") val url: String,
    @Json(name = "circuitName") val circuitName: String,
    @Json(name = "Location") val location: LocationDto
)
