package com.vozmediano.f1trivia.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.vozmediano.f1trivia.data.local.entities.CircuitEntity

@Dao
interface CircuitDao {
    @Query("SELECT * FROM circuits WHERE circuitId = :circuitId")
    suspend fun getCircuit(circuitId: String): CircuitEntity

    @Query("SELECT * FROM circuits")
    suspend fun getCircuits(): List<CircuitEntity>

    @Query("DELETE FROM circuits")
    suspend fun clearAll()

    @Upsert
    suspend fun upsertAll(circuits: List<CircuitEntity>)

    @Upsert
    suspend fun upsert(circuit: CircuitEntity)
}