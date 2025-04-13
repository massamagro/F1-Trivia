package com.vozmediano.f1trivia.data.mappers

import com.vozmediano.f1trivia.data.network.model.averagespeed.AverageSpeedDto
import com.vozmediano.f1trivia.domain.model.f1.AverageSpeed

fun AverageSpeedDto.toDomain() = AverageSpeed(
    units = units,
    speed = speed
)