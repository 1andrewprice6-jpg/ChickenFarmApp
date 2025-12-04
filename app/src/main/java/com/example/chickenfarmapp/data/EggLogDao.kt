package com.example.chickenfarmapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EggLogDao {
    @Query("SELECT * FROM egg_logs ORDER BY timestamp DESC")
    fun getAllEggLogs(): Flow<List<EggLog>>

    @Query("SELECT * FROM egg_logs WHERE chickenId = :chickenId ORDER BY timestamp DESC")
    fun getEggLogsByChicken(chickenId: Int): Flow<List<EggLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEggLog(eggLog: EggLog)

    @Delete
    suspend fun deleteEggLog(eggLog: EggLog)

    @Update
    suspend fun updateEggLog(eggLog: EggLog)
}
