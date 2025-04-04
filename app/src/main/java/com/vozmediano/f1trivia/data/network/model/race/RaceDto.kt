package com.vozmediano.f1trivia.data.network.model.race

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.vozmediano.f1trivia.data.network.model.result.ResultDto
import com.vozmediano.f1trivia.domain.model.f1.Location


@JsonClass(generateAdapter = true)
data class RaceDto(
    @Json(name = "season") val season: String ? = null,
    @Json(name = "round") val round: String ? = null,
    @Json(name = "url") val url: String ? = null,
    @Json(name = "circuitName") val circuitName: String ? = null,
    @Json(name = "location") val location: Location? = null,
    @Json(name = "date") val date: String ? = null,
    @Json(name = "time") val time: String ? = null,
    @Json(name = "Results") val results: List<ResultDto> ? = null,


    )



/*
* @JsonClass(generateAdapter = true)
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
*/