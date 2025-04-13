package com.vozmediano.f1trivia.data.mappers

import com.vozmediano.f1trivia.data.network.model.fastestlap.FastestLapDto
import com.vozmediano.f1trivia.domain.model.f1.FastestLap

fun FastestLapDto.toDomain() = FastestLap(
    rank = rank,
    lap = lap,
    time = time.toDomain(),
    averageSpeed = averageSpeed.toDomain(),
)
