package com.vozmediano.f1trivia.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "races")
data class RaceEntity(
    @PrimaryKey val raceId: String,
    val season: String,
    val round: String,
    val url: String? = null,
    val raceName : String,
    @Embedded val circuit: CircuitEntity,
    val date: String,
    val time: String? = null,
    @Embedded val results: List<ResultEntity>?  = emptyList()
)
