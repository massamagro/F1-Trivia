package com.vozmediano.f1trivia.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vozmediano.f1trivia.data.local.dao.CircuitDao
import com.vozmediano.f1trivia.data.local.dao.ConstructorDao
import com.vozmediano.f1trivia.data.local.dao.DriverDao
import com.vozmediano.f1trivia.data.local.dao.RaceDao
import com.vozmediano.f1trivia.data.local.dao.ResultDao
import com.vozmediano.f1trivia.data.local.entities.CircuitEntity
import com.vozmediano.f1trivia.data.local.entities.ConstructorEntity
import com.vozmediano.f1trivia.data.local.entities.DriverEntity
import com.vozmediano.f1trivia.data.local.entities.RaceEntity
import com.vozmediano.f1trivia.data.local.entities.ResultEntity

@Database(
    entities =
    [
        DriverEntity::class,
        ConstructorEntity::class,
        CircuitEntity::class,
        ResultEntity::class,
        RaceEntity::class
    ],
    version = 2
)
abstract class F1Database : RoomDatabase() {
    abstract fun driverDao(): DriverDao
    abstract fun constructorDao(): ConstructorDao
    abstract fun circuitDao(): CircuitDao
    abstract fun resultDao(): ResultDao
    abstract fun raceDao(): RaceDao
}
