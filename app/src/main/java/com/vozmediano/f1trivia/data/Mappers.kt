package com.vozmediano.f1trivia.data

import com.vozmediano.f1trivia.data.local.entities.DriverEntity
import com.vozmediano.f1trivia.data.network.model.DriversResponse
import com.vozmediano.f1trivia.domain.model.Driver

fun DriversResponse.toDomain() = Driver(
    broadcastName = broadcast_name ?: "",
    countryCode = country_code ?: "",
    driverNumber = driver_number,
    firstName = first_name ?: "",
    fullName = full_name ?: "",
    headshotUrl = headshot_url ?: "",
    lastName = last_name ?: "",
    meetingKey = meeting_key ?: "latest",
    nameAcronym = name_acronym ?: "",
    sessionKey = session_key ?: "latest",
    teamColour = team_colour ?: "",
    teamName = team_name ?: ""
)

fun Driver.toDatabase() = DriverEntity(
    driver_number = driverNumber,
    broadcast_name = broadcastName,
    country_code = countryCode,
    first_name = firstName,
    full_name = fullName,
    headshot_url = headshotUrl,
    last_name = lastName,
    meeting_key = meetingKey,
    name_acronym = nameAcronym,
    session_key = sessionKey,
    team_colour = teamColour,
    team_name = teamName
)

fun DriverEntity.toDomain() = Driver(
    broadcastName = broadcast_name,
    countryCode = country_code,
    driverNumber = driver_number,
    firstName = first_name,
    fullName = full_name,
    headshotUrl = headshot_url,
    lastName = last_name,
    meetingKey = meeting_key,
    nameAcronym = name_acronym,
    sessionKey = session_key,
    teamColour = team_colour,
    teamName = team_name
)
