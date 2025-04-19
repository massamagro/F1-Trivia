package com.vozmediano.f1trivia.data.mappers

import com.vozmediano.f1trivia.data.local.entities.RaceEntity
import com.vozmediano.f1trivia.data.network.model.race.RaceDto
import com.vozmediano.f1trivia.domain.model.f1.Race


fun RaceDto.toDomain() = Race(
    raceId = "$season-$round",
    season = season,
    round = round,
    url = url,
    raceName = raceName,
    circuit = circuitDto.toDomain(),
    date = date,
    results = resultsDto?.map { it.toDomain(season, round, circuitDto, raceName) } ?: emptyList() ,

)

fun Race.toDatabase() = RaceEntity(
    raceId = raceId,
    season = season,
    round = round,
    url = url,
    raceName = raceName,
    circuit = circuit.toDatabase(),
    date = date,
    results = results?.map { it.toDatabase() }
)

fun RaceEntity.toDomain() = Race(
    raceId = raceId,
    season = season,
    round = round,
    url = url,
    raceName = raceName,
    circuit = circuit.toDomain(),
    date = date,
    results = results?.map { it.toDomain() } ?: emptyList()
)

