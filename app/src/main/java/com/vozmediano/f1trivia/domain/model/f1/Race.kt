package com.vozmediano.f1trivia.domain.model.f1

import androidx.room.Embedded

data class Race (
    val season : String,
    val round : String,
    val url : String,
    val raceName : String,
    @Embedded val circuit : Circuit ?= null,
    val date : String,
    @Embedded val results : List<Result>
)