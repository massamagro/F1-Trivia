package com.vozmediano.f1trivia.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "constructors")
data class ConstructorEntity(
    @PrimaryKey val constructorId: String,
    val url: String,
    val name: String,
    val nationality: String
)