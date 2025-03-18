package com.vozmediano.f1trivia.data.network.model

data class DriverTableResponse(
    val season: String,
    val Drivers: List<DriversResponse>
)
