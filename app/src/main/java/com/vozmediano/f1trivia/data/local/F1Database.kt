package com.vozmediano.f1trivia.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vozmediano.f1trivia.data.local.dao.ConstructorDao
import com.vozmediano.f1trivia.data.local.dao.DriverDao
import com.vozmediano.f1trivia.data.local.entities.ConstructorEntity
import com.vozmediano.f1trivia.data.local.entities.DriverEntity

@Database(
    //entities = [DriverEntity::class, RaceEntity::class, SessionEntity::class],
    entities = [DriverEntity::class, ConstructorEntity::class],
    version = 1
)
abstract class F1Database : RoomDatabase() {
    abstract fun driverDao(): DriverDao
    abstract fun constructorDao(): ConstructorDao
    //abstract fun raceDao(): RaceDao
    //abstract fun sessionDao(): SessionDao
}
