package com.vozmediano.f1trivia.data.mappers

import com.vozmediano.f1trivia.data.local.entities.TimeEntity
import com.vozmediano.f1trivia.data.network.model.time.TimeDto
import com.vozmediano.f1trivia.domain.model.f1.Time

fun TimeDto.toDomain() = Time(
    time = time,
    millis = millis
)

fun Time.toDatabase() = TimeEntity(
    totalTime = time,
    millis = millis
)

fun TimeEntity.toDomain() = Time(
    time = totalTime,
    millis = millis
)