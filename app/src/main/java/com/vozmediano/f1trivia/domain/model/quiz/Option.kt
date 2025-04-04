package com.vozmediano.f1trivia.domain.model.quiz

data class Option(
    val id: Int,
    val shortText: String,
    val longText: String,
    val isCorrect: Boolean
)
