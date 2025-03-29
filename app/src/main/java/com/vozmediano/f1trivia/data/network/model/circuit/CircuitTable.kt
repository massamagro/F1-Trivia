package com.vozmediano.f1trivia.data.network.model.circuit

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CircuitTable(
    @Json(name = "Circuits") val circuitDtos: List<CircuitDto> ? = null
)
