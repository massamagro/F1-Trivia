package com.vozmediano.f1trivia.domain.model.f1

data class FastestLap(
    val rank: String,
    val lap: String,
    val time: Time,
    val averageSpeed: AverageSpeed
)
