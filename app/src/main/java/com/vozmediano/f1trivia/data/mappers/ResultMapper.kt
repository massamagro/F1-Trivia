package com.vozmediano.f1trivia.data.mappers

import com.vozmediano.f1trivia.data.local.entities.ResultEntity
import com.vozmediano.f1trivia.data.network.model.circuit.CircuitDto
import com.vozmediano.f1trivia.data.network.model.result.ResultDto
import com.vozmediano.f1trivia.domain.model.f1.Result

fun ResultDto.toDomain(
    season: String,
    round: String,
    circuitDto: CircuitDto,
    raceName: String
) = Result(

    number = number,
    position = position,
    positionText = positionText,
    points = points,
    driver = driverDto.toDomain(),
    constructor = constructorDto.toDomain(),
    grid = grid,
    laps = laps,
    status = status,
    //fastestLap = fastestLapDto?.toDomain(),
    season = season,
    round = round,
    circuit = circuitDto.toDomain(),
    raceName = raceName
)

fun Result.toDatabase() = ResultEntity(
    id = "${season}_${round}_${driver.driverId}",
    number = number,
    position = position,
    positionText = positionText,
    points = points,
    driver = driver.toDatabase(),
    constructor = constructor.toDatabase(),
    grid = grid,
    laps = laps,
    status = status,
    //fastestLap = fastestLap?.toDatabase(),
    season = season,
    round = round,
    circuit = circuit.toDatabase(),
    raceName = raceName
)

fun ResultEntity.toDomain() = Result(
    number = number,
    position = position,
    positionText = positionText,
    points = points,
    driver = driver.toDomain(),
    constructor = constructor.toDomain(),
    grid = grid,
    laps = laps,
    status = status,
    //fastestLap = fastestLap?.toDomain(),
    season = season,
    round = round,
    circuit = circuit.toDomain(),
    raceName = raceName
)