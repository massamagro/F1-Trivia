package com.vozmediano.f1trivia.domain.model.f1

import androidx.room.Embedded

data class Race(
    val raceId: String,
    val season: String,
    val round: String,
    val url: String? = null,
    val raceName: String,
    @Embedded val circuit: Circuit,
    val date: String,
    @Embedded val results: List<Result>? = null
)