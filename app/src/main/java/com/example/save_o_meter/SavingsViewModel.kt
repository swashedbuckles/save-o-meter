package com.example.save_o_meter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SavingsViewModel(private val dataManager: SavingsDataManager) : ViewModel() {
    var currentSavings by mutableFloatStateOf(dataManager.getCurrentSavings())
        private set
    var currentSavingsGoal by mutableFloatStateOf(dataManager.getSavingsGoal())
        private set

    fun addSavings(amount: Float) {
        if(amount > 0) {
            currentSavings += amount
            dataManager.saveCurrentSavings(currentSavings)
        }
    }

    fun removeSavings(amount: Float) {
        if(amount > 0) {
            currentSavings = maxOf(0f, currentSavings - amount)
            dataManager.saveCurrentSavings(currentSavings)
        }
    }

    fun updateGoal(newGoal: Float) {
        if (newGoal > 0) {
            currentSavingsGoal = newGoal
            dataManager.saveSavingGoal(newGoal)
        }
    }
}