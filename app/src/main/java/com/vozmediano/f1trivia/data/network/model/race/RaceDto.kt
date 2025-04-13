package com.vozmediano.f1trivia.data.network.model.race

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.vozmediano.f1trivia.data.network.model.circuit.CircuitDto
import com.vozmediano.f1trivia.data.network.model.result.ResultDto
import com.vozmediano.f1trivia.domain.model.f1.Location


@JsonClass(generateAdapter = true)
data class RaceDto(
    @Json(name = "season") val season: String,
    @Json(name = "round") val round: String,
    @Json(name = "url") val url: String,
    @Json(name = "raceName") val raceName: String,
    @Json(name = "circuit") val circuit: CircuitDto ?= null,
    @Json(name = "location") val location: Location ? = null,
    @Json(name = "date") val date: String,
    @Json(name = "Results") val results: List<ResultDto>,


    )
