
package com.example.save_o_meter

import java.util.UUID

/**
 * Data class representing a savings goal
 */
data class SavingsGoal(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val targetAmount: Float,
    val currentAmount: Float = 0f
) {
    /**
     * Calculate progress as a percentage (0.0 to 1.0)
     */
    val progress: Float
        get() = if (targetAmount > 0) currentAmount / targetAmount else 0f

    /**
     * Check if the goal has been reached
     */
    val isReached: Boolean
        get() = progress >= 1f

    /**
     * Get remaining amount needed to reach goal
     */
    val remainingAmount: Float
        get() = maxOf(0f, targetAmount - currentAmount)

    /**
     * Get progress as percentage (0-100)
     */
    val progressPercentage: Int
        get() = (progress * 100).toInt()
}