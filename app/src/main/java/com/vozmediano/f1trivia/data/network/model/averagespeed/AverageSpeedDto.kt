package com.vozmediano.f1trivia.data.network.model.averagespeed

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AverageSpeedDto (
    val units: String,
    val speed: String
)
