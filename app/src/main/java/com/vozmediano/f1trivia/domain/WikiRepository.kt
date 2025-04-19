package com.vozmediano.f1trivia.domain

interface WikiRepository {
    suspend fun getImage(title: String): String
}