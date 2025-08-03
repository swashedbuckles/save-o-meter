package com.example.save_o_meter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

/**
 * Dialog for editing savings goal name and amount
 */
@Composable
fun GoalEditDialog(
    currentGoalName: String,
    currentGoalAmount: Float,
    onDismiss: () -> Unit,
    onSave: (String, Float) -> Unit
) {
    // Local state for the dialog inputs
    var goalName by remember { mutableStateOf(currentGoalName) }
    var goalAmount by remember { mutableStateOf(currentGoalAmount.toString()) }

    // Validation - check if inputs are valid
    val isValidName = goalName.trim().isNotEmpty()
    val isValidAmount = goalAmount.toFloatOrNull()?.let { it > 0 } == true
    val canSave = isValidName && isValidAmount

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Edit Savings Goal",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Goal name input
                OutlinedTextField(
                    value = goalName,
                    onValueChange = { goalName = it },
                    label = { Text("Goal Name") },
                    placeholder = { Text("e.g., Vacation Fund") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !isValidName && goalName.isNotBlank(),
                    supportingText = if (!isValidName && goalName.isNotBlank()) {
                        { Text("Goal name cannot be empty") }
                    } else null
                )

                // Goal amount input
                OutlinedTextField(
                    value = goalAmount,
                    onValueChange = { goalAmount = it },
                    label = { Text("Goal Amount") },
                    placeholder = { Text("500") },
                    prefix = { Text("$") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    isError = !isValidAmount && goalAmount.isNotBlank(),
                    supportingText = if (!isValidAmount && goalAmount.isNotBlank()) {
                        { Text("Amount must be greater than 0") }
                    } else null
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amount = goalAmount.toFloat()
                    onSave(goalName.trim(), amount)
                },
                enabled = canSave
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

/**
 * Dialog for confirming data reset operations
 */
@Composable
fun ResetConfirmationDialog(
    onDismiss: () -> Unit,
    onResetSavings: () -> Unit,
    onResetAll: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Reset Data",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "What would you like to reset?",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "• Reset Savings: Clear progress but keep your goal",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "• Reset Everything: Clear all data and return to defaults",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(
                    onClick = {
                        onResetSavings()
                        onDismiss()
                    }
                ) {
                    Text("Reset Savings")
                }
                TextButton(
                    onClick = {
                        onResetAll()
                        onDismiss()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Reset All")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
