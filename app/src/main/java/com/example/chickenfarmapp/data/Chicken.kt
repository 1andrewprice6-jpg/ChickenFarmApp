package com.example.chickenfarmapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chickens")
data class Chicken(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val breed: String
)