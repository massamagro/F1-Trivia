package com.vozmediano.f1trivia.data.mappers

import com.vozmediano.f1trivia.data.local.entities.CircuitEntity
import com.vozmediano.f1trivia.data.network.model.circuit.CircuitDto
import com.vozmediano.f1trivia.domain.model.f1.Circuit

fun CircuitDto.toDomain() = Circuit(
    circuitId = circuitId,
    url = url ?: "",
    circuitName = circuitName,
    location = location.toDomain()
)

fun Circuit.toDatabase() = CircuitEntity(
    circuitId = circuitId,
    url = url,
    circuitName = circuitName,
    location = location.toDatabase()
)

fun CircuitEntity.toDomain() = Circuit(
    circuitId = circuitId,
    url = url,
    circuitName = circuitName,
    location = location.toDomain()
)

