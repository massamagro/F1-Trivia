package com.vozmediano.f1trivia.data.network.model

data class DriversResponse(
    val broadcast_name: String,
    val country_code: String,
    val driver_number: String,
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
