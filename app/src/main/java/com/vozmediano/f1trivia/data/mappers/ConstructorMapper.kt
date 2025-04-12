package com.vozmediano.f1trivia.data.mappers

import com.vozmediano.f1trivia.data.local.entities.ConstructorEntity
import com.vozmediano.f1trivia.data.network.model.constructor.ConstructorDto
import com.vozmediano.f1trivia.domain.model.f1.Constructor

fun ConstructorDto.toDomain() = Constructor(
    constructorId = constructorId,
    url = url,
    name = name,
    nationality = nationality
)

fun Constructor.toDatabase() = ConstructorEntity(
    constructorId = constructorId,
    url = url,
    name = name,
    nationality = nationality
)

fun ConstructorEntity.toDomain() = Constructor(
    constructorId = constructorId,
    url = url,
    name = name,
    nationality = nationality
)