package com.vozmediano.f1trivia.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.vozmediano.f1trivia.data.local.entities.ConstructorEntity

@Dao
interface ConstructorDao {
    @Query("SELECT * FROM constructors WHERE constructorId = :constructorId")
    suspend fun getConstructor(constructorId: String): ConstructorEntity

    @Query("SELECT * FROM constructors")
    suspend fun getConstructors(): List<ConstructorEntity>

    @Query("DELETE FROM constructors")
    suspend fun clearAll()

    @Upsert
    suspend fun upsertAll(constructors: List<ConstructorEntity>)

    @Upsert
    suspend fun upsert(constructor: ConstructorEntity)

}