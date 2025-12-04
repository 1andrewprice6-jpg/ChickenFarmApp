package com.example.chickenfarmapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ChickenDao {
    @Query("SELECT * FROM chickens ORDER BY name ASC")
    fun getAllChickens(): Flow<List<Chicken>>

    @Query("SELECT * FROM chickens WHERE id = :id")
    suspend fun getChickenById(id: Int): Chicken?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChicken(chicken: Chicken)

    @Delete
    suspend fun deleteChicken(chicken: Chicken)

    @Update
    suspend fun updateChicken(chicken: Chicken)
}
