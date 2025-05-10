package com.vozmediano.f1trivia.domain.model.quiz

data class ScoreboardEntry(
    val username: String,
    val score: Int,
    val timestamp: Int
)
