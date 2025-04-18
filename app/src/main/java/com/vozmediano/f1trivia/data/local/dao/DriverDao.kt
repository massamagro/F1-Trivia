package com.vozmediano.f1trivia.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.vozmediano.f1trivia.data.local.entities.DriverEntity

@Dao
interface DriverDao {
    @Query("SELECT * FROM drivers WHERE driverId = :driverId")
    suspend fun getDriver(driverId: String): DriverEntity

    @Query("SELECT * FROM drivers")
    suspend fun getDrivers(): List<DriverEntity>

    @Query("DELETE FROM drivers")
    suspend fun clearAll()

    @Upsert
    suspend fun upsertAll(drivers: List<DriverEntity>)

    @Upsert
    suspend fun upsert(driver: DriverEntity)

}