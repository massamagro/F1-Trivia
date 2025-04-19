package com.vozmediano.f1trivia.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.vozmediano.f1trivia.data.local.entities.ImageEntity

@Dao
interface ImageDao {
    @Query("SELECT * FROM images WHERE title = :title")
    suspend fun getImage(title: String): ImageEntity
}