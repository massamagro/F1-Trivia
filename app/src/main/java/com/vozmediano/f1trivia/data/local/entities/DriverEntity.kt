package com.vozmediano.f1trivia.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drivers")
data class DriverEntity(
    @PrimaryKey val driverId: String,
    val permanentNumber: String,
    val code: String,
    val driverUrl: String,
    val givenName: String,
    val familyName: String,
    val dateOfBirth: String,
    val driverNationality: String
)
