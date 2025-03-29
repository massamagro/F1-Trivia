package com.vozmediano.f1trivia.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "circuits")
class CircuitEntity (
    @PrimaryKey val circuitId: String,
    val url: String,
    val circuitName: String,
    @Embedded val location: LocationEntity
    )