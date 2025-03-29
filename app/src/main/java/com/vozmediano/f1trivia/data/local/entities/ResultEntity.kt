package com.vozmediano.f1trivia.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity

@Entity(tableName = "results")
data class ResultEntity(
    val number : String,
    val position : String,
    val positionText : String,
    val points : String,
    val driver : DriverEntity,
    val constructor : ConstructorEntity,
    val grid : String,
    val laps : String,
    val status : String,
    @Embedded val fastestLap: FastestLapEntity
)
