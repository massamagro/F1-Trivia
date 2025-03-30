package com.vozmediano.f1trivia.data.mappers

import com.vozmediano.f1trivia.data.local.entities.DriverEntity
import com.vozmediano.f1trivia.data.network.model.driver.DriverDto
import com.vozmediano.f1trivia.domain.model.f1.Driver


fun DriverDto.toDomain() = Driver(
    driverId = driverId,
    permanentNumber = permanentNumber ?: "",
    code = code ?: "",
    url = url ?: "",
    givenName = givenName,
    familyName = familyName,
    dateOfBirth = dateOfBirth ?: "",
    nationality = nationality ?: ""
)

fun Driver.toDatabase() = DriverEntity(
    driverId = driverId,
    permanentNumber = permanentNumber,
    code = code,
    url = url,
    givenName = givenName,
    familyName = familyName,
    dateOfBirth = dateOfBirth,
    nationality = nationality
)

fun DriverEntity.toDomain() = Driver(
    driverId = driverId,
    permanentNumber = permanentNumber,
    code = code,
    url = url,
    givenName = givenName,
    familyName = familyName,
    dateOfBirth = dateOfBirth,
    nationality = nationality
)
