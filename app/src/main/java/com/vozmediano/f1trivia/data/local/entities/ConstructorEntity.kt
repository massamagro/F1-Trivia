package com.vozmediano.f1trivia.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "constructors")
data class ConstructorEntity(
    @PrimaryKey val constructorId: String,
    val constructorUrl: String,
    val name: String,
    val constructorNationality: String
)