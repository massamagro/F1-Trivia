package com.vozmediano.f1trivia.domain.model.quiz

import com.google.firebase.Timestamp

data class ScoreboardEntry(
    val username: String,
    val score: Int,
    val timestamp: Int
)
