package com.vozmediano.f1trivia.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.vozmediano.f1trivia.data.local.entities.ResultEntity

@Dao
interface ResultDao {
    @Query("SELECT * FROM results WHERE season = :season AND round = :round")
    suspend fun getResultsBySeasonAndRound(season:String,round:String): List<ResultEntity>

    @Query("SELECT * FROM results WHERE season = :season")
    suspend fun getResultsBySeasonAndRound(season: String): List<ResultEntity>

    @Upsert
    suspend fun upsertAll(results: List<ResultEntity>)

    @Upsert
    suspend fun upsert(result: ResultEntity)

    @Query("DELETE FROM results")
    suspend fun clearAll()

}