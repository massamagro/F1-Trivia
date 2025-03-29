package com.vozmediano.f1trivia.data.mappers

import com.vozmediano.f1trivia.data.local.entities.LocationEntity
import com.vozmediano.f1trivia.data.network.model.circuit.LocationDto
import com.vozmediano.f1trivia.domain.model.Location


fun LocationDto.toDomain() = Location(
    lat = lat,
    long = long,
    locality = locality,
    country = country
)

fun Location.toDatabase() = LocationEntity(
    lat = lat,
    long = long,
    locality = locality,
    country = country
)

fun LocationEntity.toDomain() = Location(
    lat = lat,
    long = long,
    locality = locality,
    country = country
)


