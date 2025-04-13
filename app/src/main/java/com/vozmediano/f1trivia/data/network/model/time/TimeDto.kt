package com.vozmediano.f1trivia.data.network.model.time

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TimeDto(
    @Json(name = "millis") val millis: String ? = null,
    @Json(name = "time") val time: String
)
