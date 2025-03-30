package com.vozmediano.f1trivia.domain.model.f1

import androidx.room.Embedded

data class Circuit(
    val circuitId: String,
    val url: String,
    val circuitName: String,
    @Embedded val location: Location
)
