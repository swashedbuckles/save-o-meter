
package com.example.save_o_meter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

/**
 * Displays the savings goal information card
 */
@Composable
fun SavingsInfoCard(
    goalName: String,
    currentSavings: Float,
    goalAmount: Float,
    progress: Float,
    isGoalReached: Boolean,
    remainingAmount: Float,
    onEditGoal: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Goal name and edit button row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = goalName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                TextButton(onClick = onEditGoal) {
                    Text("Edit")
                }
            }

            // Goal amount
            Text(
                text = "Goal: $${goalAmount.toInt()}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            // Current savings
            Text(
                text = "Saved: $${currentSavings.toInt()}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            // Progress and remaining amount row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${(progress * 100).toInt()}% Complete",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isGoalReached) Color.Red else MaterialTheme.colorScheme.onSurface
                )

                if (!isGoalReached) {
                    Text(
                        text = "$${remainingAmount.toInt()} to go",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * Input controls for adding/removing money
 */
@Composable
fun SavingsInputControls(
    inputAmount: String,
    onInputAmountChange: (String) -> Unit,
    onAddMoney: (Float) -> Unit,
    onRemoveMoney: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = inputAmount,
            onValueChange = onInputAmountChange,
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f),
            prefix = { Text("$") }
        )

        Button(
            onClick = {
                val amount = inputAmount.toFloatOrNull()
                if (amount != null && amount > 0) {
                    onAddMoney(amount)
                }
            },
            enabled = inputAmount.toFloatOrNull()?.let { it > 0 } == true
        ) {
            Text("Add")
        }

        Button(
            onClick = {
                val amount = inputAmount.toFloatOrNull()
                if (amount != null && amount > 0) {
                    onRemoveMoney(amount)
                }
            },
            enabled = inputAmount.toFloatOrNull()?.let { it > 0 } == true
        ) {
            Text("Remove")
        }
    }
}

/**
 * Reset data button
 */
@Composable
fun ResetDataButton(
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(onClick = onReset) {
            Text(
                text = "Reset Data",
                color = Color.Red
            )
        }
    }
}
