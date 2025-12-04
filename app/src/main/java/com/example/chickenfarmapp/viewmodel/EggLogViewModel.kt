package com.example.chickenfarmapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.chickenfarmapp.data.EggLog
import com.example.chickenfarmapp.data.EggLogDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

class EggLogViewModel(private val eggLogDao: EggLogDao) : ViewModel() {
    val eggLogs: StateFlow<List<EggLog>> = eggLogDao.getAllEggLogs()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addEggLog(chickenId: Int, count: Int) {
        viewModelScope.launch {
            val newEggLog = EggLog(
                chickenId = chickenId,
                timestamp = Date(),
                count = count
            )
            eggLogDao.insertEggLog(newEggLog)
        }
    }

    fun deleteEggLog(eggLog: EggLog) {
        viewModelScope.launch {
            eggLogDao.deleteEggLog(eggLog)
        }
    }
}

class EggLogViewModelFactory(private val eggLogDao: EggLogDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EggLogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EggLogViewModel(eggLogDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}