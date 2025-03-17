package com.vozmediano.f1trivia.domain.model

data class Driver(
    val broadcastName: String,
    val countryCode: String,
    val driverNumber: String,
    val firstName: String,
    val fullName: String,
    val headshotUrl: String,
    val lastName: String,
    val meetingKey: String,
    val nameAcronym: String,
    val sessionKey: String,
    val teamColour: String,
    val teamName: String
)
