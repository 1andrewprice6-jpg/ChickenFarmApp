package com.example.chickenfarmapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.chickenfarmapp.data.EggLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Date

class EggLogViewModel : ViewModel() {
    private val _eggLogs = MutableStateFlow<List<EggLog>>(emptyList())
    val eggLogs: StateFlow<List<EggLog>> = _eggLogs

    fun addEggLog(chickenId: Int, count: Int) {
        val newEggLog = EggLog(
            id = _eggLogs.value.size + 1,
            chickenId = chickenId,
            timestamp = Date(),
            count = count
        )
        _eggLogs.value = _eggLogs.value + newEggLog
    }
}