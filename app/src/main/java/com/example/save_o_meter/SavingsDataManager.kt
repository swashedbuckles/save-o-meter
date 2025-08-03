package com.example.save_o_meter

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SavingsDataManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("savaings_data", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_CURRENT_SAVINGS = "current_savings"
        private const val KEY_SAVINGS_GOAL = "savings_goal"
        private const val KEY_GOAL_NAME = "goal_name"
        private const val DEFAULT_GOAL = 500f
        private const val DEFAULT_GOAL_NAME = "My Savings Goal"
    }

    fun getCurrentSavings(): Float {
        return sharedPreferences.getFloat(KEY_CURRENT_SAVINGS, 0f)
    }

    fun getSavingsGoal(): Float {
        return sharedPreferences.getFloat(KEY_SAVINGS_GOAL, DEFAULT_GOAL)
    }

    fun getGoalName(): String {
        return sharedPreferences.getString(KEY_GOAL_NAME, DEFAULT_GOAL_NAME) ?: DEFAULT_GOAL_NAME
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

    fun saveGoalName(name: String) {
        sharedPreferences.edit {
            putString(KEY_GOAL_NAME, name)
        }
    }

    fun saveGoalData(amount: Float, name: String) {
        sharedPreferences.edit {
            putFloat(KEY_SAVINGS_GOAL, amount)
            putString(KEY_GOAL_NAME, name)
        }
    }

    fun resetCurrentSavings() {
        sharedPreferences.edit {
            putFloat(KEY_CURRENT_SAVINGS, 0f)
        }
    }

    fun resetAllData() {
        sharedPreferences.edit {
            putFloat(KEY_CURRENT_SAVINGS, 0f)
            putFloat(KEY_SAVINGS_GOAL, DEFAULT_GOAL)
            putString(KEY_GOAL_NAME, DEFAULT_GOAL_NAME)
        }
    }

    fun hasExistingData(): Boolean {
        return sharedPreferences.contains(KEY_CURRENT_SAVINGS) ||
               sharedPreferences.contains(KEY_SAVINGS_GOAL) ||
               sharedPreferences.contains(KEY_GOAL_NAME)
    }
}