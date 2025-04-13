package com.vozmediano.f1trivia.data.mappers

import com.vozmediano.f1trivia.data.network.model.race.RaceDto
import com.vozmediano.f1trivia.domain.model.f1.Race


fun RaceDto.toDomain() = Race(
    season = season,
    round = round,
    url = url,
    raceName = raceName,
    circuit = circuit?.toDomain(),
    date = date,
    results = results.map { it.toDomain() }
)