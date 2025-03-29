package com.vozmediano.f1trivia.data.network.model.circuit

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationDto(
    @Json(name = "lat") val lat: String,
    @Json(name = "long") val long: String,
    @Json(name = "locality") val locality: String,
    @Json(name = "country") val country: String
)
