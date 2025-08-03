package com.example.save_o_meter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SavingsViewModel(private val dataManager: SavingsDataManager) : ViewModel() {
    var currentSavings by mutableFloatStateOf(dataManager.getCurrentSavings())
        private set
    var currentSavingsGoal by mutableFloatStateOf(dataManager.getSavingsGoal())
        private set

    var goalName by mutableStateOf(dataManager.getGoalName())
        private set

    var showCelebration by mutableStateOf(false)
        private set

    private var wasGoalReached = false
    fun addSavings(amount: Float) {
        if(amount > 0) {
            val previousSavings = currentSavings
            currentSavings += amount
            dataManager.saveCurrentSavings(currentSavings)

            checkForGoalCelebration(previousSavings)
        }
    }

    fun removeSavings(amount: Float) {
        if(amount > 0) {
            currentSavings = maxOf(0f, currentSavings - amount)
            dataManager.saveCurrentSavings(currentSavings)
        }
    }

    fun updateGoalAmount(newGoal: Float) {
        if (newGoal > 0) {
            currentSavingsGoal = newGoal
            dataManager.saveSavingGoal(newGoal)
        }
    }

    fun updateGoalName(newName: String) {
        val trimmedName = newName.trim()
        if (trimmedName.isNotEmpty()) {
            goalName = trimmedName
            dataManager.saveGoalName(trimmedName)
        }
    }

    fun updateGoal(newAmount: Float, newName: String) {
        val trimmedName = newName.trim()
        if(newAmount > 0 && trimmedName.isNotEmpty()) {
            currentSavingsGoal = newAmount
            goalName = trimmedName
            dataManager.saveGoalData(newAmount, trimmedName)
        }
    }

    fun resetSavingsProgress() {
        currentSavings = 0f
        dataManager.resetCurrentSavings()
        wasGoalReached = false
        showCelebration = false
    }

    fun resetAllData() {
        currentSavings = 0f
        currentSavingsGoal = 500f
        goalName = "My Savings Goal"
        dataManager.resetAllData()
        wasGoalReached = false
        showCelebration = false
    }

    fun isGoalReached(): Boolean {
        return currentSavingsGoal > 0 && currentSavings >= currentSavingsGoal
    }

    fun getProgressPercentage(): Int {
        return if(currentSavingsGoal > 0) {
            ((currentSavings / currentSavingsGoal) * 100).toInt()
        } else {
            0
        }
    }

    fun getRemainingAmount(): Float {
        return maxOf(0f, currentSavingsGoal - currentSavings)
    }

    private fun checkForGoalCelebration(previousAmount: Float) {
        val goalReached = currentSavingsGoal > 0 && currentSavings >= currentSavingsGoal
        val justReachedGoal = goalReached && previousAmount < currentSavingsGoal

        if (justReachedGoal && !wasGoalReached) {
            showCelebration = true
            wasGoalReached = true
        }
    }

    fun hideCelebration() {
        showCelebration = false
    }

}