package com.vozmediano.f1trivia.domain.model.quiz

data class Option(
    val id: Int,
    val text: String,
    val isCorrect: Boolean
)
