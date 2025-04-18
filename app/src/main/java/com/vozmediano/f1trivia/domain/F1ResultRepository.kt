package com.vozmediano.f1trivia.domain

import com.vozmediano.f1trivia.domain.model.f1.Result


interface F1ResultRepository {
    suspend fun getResultsBySeason(season: String): List<Result>
}