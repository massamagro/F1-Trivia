package com.vozmediano.f1trivia.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drivers")
data class DriverEntity(
    @PrimaryKey val driver_number: String,
    val broadcast_name: String,
    val country_code: String,
    val first_name: String,
    val full_name: String,
    val headshot_url: String,
    val last_name: String,
    val meeting_key: String,
    val name_acronym: String,
    val session_key: String,
    val team_colour: String,
    val team_name: String
)
