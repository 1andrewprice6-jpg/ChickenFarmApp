package com.example.chickenfarmapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "egg_logs")
data class EggLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val chickenId: Int,
    val timestamp: Date,
    val count: Int
)