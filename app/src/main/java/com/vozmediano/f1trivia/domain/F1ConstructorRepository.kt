package com.vozmediano.f1trivia.domain

import com.vozmediano.f1trivia.domain.model.f1.Constructor

interface F1ConstructorRepository {
    //CONSTRUCTORS
    suspend fun getConstructors(): List<Constructor>
    suspend fun getConstructorById(constructorId: String): Constructor
    suspend fun getConstructorsBySeason(season: String): List<Constructor>
}