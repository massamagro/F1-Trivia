package com.vozmediano.f1trivia.data.mappers

import com.vozmediano.f1trivia.data.network.model.result.ResultDto
import com.vozmediano.f1trivia.domain.model.f1.Result

fun ResultDto.toDomain() = Result(
    number = number,
    position = position,
    positionText = positionText,
    points = points,
    driver = driverDto.toDomain(),
    constructor = constructorDto.toDomain(),
    grid = grid,
    laps = laps,
    status = status,
    fastestLap = fastestLapDto?.toDomain()
)