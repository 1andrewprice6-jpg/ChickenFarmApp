package com.example.chickenfarmapp.data

import java.util.Date

data class EggLog(
    val id: Int,
    val chickenId: Int,
    val timestamp: Date,
    val count: Int
)