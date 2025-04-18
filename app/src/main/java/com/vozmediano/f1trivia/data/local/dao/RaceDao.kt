package com.vozmediano.f1trivia.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.vozmediano.f1trivia.data.local.entities.RaceEntity

@Dao
interface RaceDao {
    @Query("SELECT * FROM races WHERE season = :season AND round = :round")
    suspend fun getRacesBySeasonAndRound(season: String, round: String): RaceEntity
}