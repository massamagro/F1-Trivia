package com.vozmediano.f1trivia.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DriverDto(
    @Json(name = "driverId") val driverId: String,
    @Json(name = "permanentNumber") val permanentNumber: String,
    @Json(name = "code") val code: String,
    @Json(name = "url") val url: String,
    @Json(name = "givenName") val givenName: String,
    @Json(name = "familyName") val familyName: String,
    @Json(name = "dateOfBirth") val dateOfBirth: String,
    @Json(name = "nationality") val nationality: String
)
