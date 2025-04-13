package com.vozmediano.f1trivia.domain.model.f1

import androidx.room.Embedded

data class FastestLap(
    val rank: String,
    val lap: String,
    @Embedded val time: Time,
    @Embedded val averageSpeed: AverageSpeed
)
