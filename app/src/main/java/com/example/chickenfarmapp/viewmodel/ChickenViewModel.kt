package com.example.chickenfarmapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.chickenfarmapp.data.Chicken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChickenViewModel : ViewModel() {
    private val _chickens = MutableStateFlow<List<Chicken>>(emptyList())
    val chickens: StateFlow<List<Chicken>> = _chickens

    fun addChicken(name: String, breed: String) {
        val newChicken = Chicken(
            id = _chickens.value.size + 1,
            name = name,
            breed = breed
        )
        _chickens.value = _chickens.value + newChicken
    }
}