package com.vozmediano.f1trivia.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey val title: String,
    val imageUrl: String
)
