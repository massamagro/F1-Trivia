package com.vozmediano.f1trivia.data.network.model.driver

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DriverDto(
    @Json(name = "driverId") val driverId: String,
    @Json(name = "permanentNumber") val permanentNumber: String ? = null,
    @Json(name = "code") val code: String ? = null,
    @Json(name = "url") val url: String ? = null,
    @Json(name = "givenName") val givenName: String,
    @Json(name = "familyName") val familyName: String,
    @Json(name = "dateOfBirth") val dateOfBirth: String ? = null,
    @Json(name = "nationality") val nationality: String ? = null
)
