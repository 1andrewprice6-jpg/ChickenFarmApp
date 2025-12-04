package com.example.chickenfarmapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.chickenfarmapp.data.Chicken
import com.example.chickenfarmapp.data.ChickenDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChickenViewModel(private val chickenDao: ChickenDao) : ViewModel() {
    val chickens: StateFlow<List<Chicken>> = chickenDao.getAllChickens()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addChicken(name: String, breed: String) {
        viewModelScope.launch {
            val newChicken = Chicken(
                name = name,
                breed = breed
            )
            chickenDao.insertChicken(newChicken)
        }
    }

    fun deleteChicken(chicken: Chicken) {
        viewModelScope.launch {
            chickenDao.deleteChicken(chicken)
        }
    }
}

class ChickenViewModelFactory(private val chickenDao: ChickenDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChickenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChickenViewModel(chickenDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}