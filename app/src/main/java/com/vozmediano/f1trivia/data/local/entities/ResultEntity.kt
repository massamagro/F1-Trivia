package com.vozmediano.f1trivia.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "results")
data class ResultEntity(
    @PrimaryKey val id: String,
    val number : String,
    val position : String,
    val positionText : String,
    val points : String,
    @Embedded val driver : DriverEntity,
    @Embedded val constructor : ConstructorEntity,
    val grid : String,
    val laps : String,
    val status : String,
    @Embedded val time: TimeEntity ? = null,
    //@Embedded val fastestLap: FastestLapEntity ? = null,

    //extra data
    val season : String? = null,
    val round : String? = null,
    @Embedded val circuit: CircuitEntity,
    val raceName: String
)
