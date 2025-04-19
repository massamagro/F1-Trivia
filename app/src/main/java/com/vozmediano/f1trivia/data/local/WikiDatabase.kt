package com.vozmediano.f1trivia.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vozmediano.f1trivia.data.local.dao.ImageDao
import com.vozmediano.f1trivia.data.local.entities.ImageEntity

@Database(
    entities = [ImageEntity::class],
    version = 1
)
abstract class WikiDatabase: RoomDatabase() {
    abstract fun imageDao(): ImageDao
}