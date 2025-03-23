package com.vozmediano.f1trivia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.vozmediano.f1trivia.data.local.entities.DriverEntity
import com.vozmediano.f1trivia.data.network.model.DriversResponse

@Dao
interface DriverDao {
    @Query("SELECT * FROM drivers WHERE driverId = :driverId")
    suspend fun getDriver(driverId: String): DriverEntity

    @Query("SELECT * FROM drivers")
    suspend fun getDrivers(): List<DriverEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(drivers: List<DriverEntity>)

    @Query("DELETE FROM drivers")
    suspend fun clearAll()

    @Upsert
    suspend fun upsertAll(drivers: List<DriverEntity>)

}