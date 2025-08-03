package com.example.save_o_meter

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SavingsDataManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("savaings_data", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_CURRENT_SAVINGS = "current_savings"
        private const val KEY_SAVINGS_GOAL = "savings_goal"
        private const val DEFAULT_GOAL = 500f
    }

    fun getCurrentSavings(): Float {
        return sharedPreferences.getFloat(KEY_CURRENT_SAVINGS, 0f)
    }

    fun getSavingsGoal(): Float {
        return sharedPreferences.getFloat(KEY_SAVINGS_GOAL, DEFAULT_GOAL)
    }

    fun saveCurrentSavings(amount: Float) {
        sharedPreferences.edit {
            putFloat(KEY_CURRENT_SAVINGS, amount)
        }
    }

    fun saveSavingGoal(goal: Float) {
        sharedPreferences.edit {
            putFloat(KEY_SAVINGS_GOAL, goal)
        }
    }
}