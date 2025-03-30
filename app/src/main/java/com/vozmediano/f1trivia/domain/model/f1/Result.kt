package com.vozmediano.f1trivia.domain.model.f1

data class Result(
    val number : String,
    val position : String,
    val positionText : String,
    val points : String,
    val driver : Driver,
    val constructor : Constructor,
    val grid : String,
    val laps : String,
    val status : String,
    val fastestLap: FastestLap
)
