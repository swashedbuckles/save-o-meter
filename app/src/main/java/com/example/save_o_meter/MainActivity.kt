package com.example.save_o_meter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.save_o_meter.ui.theme.SaveoMeterTheme
import kotlin.math.min

/**
 * Main Activity - Entry point of the application
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SaveoMeterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SavingsThermometerScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

/**
 * Main screen composable that coordinates all UI components
 */
@Composable
fun SavingsThermometerScreen(modifier: Modifier = Modifier) {
    // Initialize data layer and view model
    val context = LocalContext.current
    val dataManager = remember { SavingsDataManager(context) }
    val viewModel: SavingsViewModel = viewModel { SavingsViewModel(dataManager) }

    // Local state for input and dialogs
    var inputAmount by remember { mutableStateOf("") }
    var showGoalEditDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    // Calculate progress and animation
    val progress = if (viewModel.currentSavingsGoal > 0) {
        viewModel.currentSavings / viewModel.currentSavingsGoal
    } else {
        0f
    }
    val isGoalReached = progress >= 1f

    val animatedProgress by animateFloatAsState(
        targetValue = min(progress, 1f),
        animationSpec = tween(durationMillis = 1000, easing = EaseOutCubic),
        label = "progress_animation"
    )

    Box(modifier = modifier.fillMaxSize())
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App title
        Text(
            text = "Savings Thermometer",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Savings information card
        SavingsInfoCard(
            goalName = viewModel.goalName,
            currentSavings = viewModel.currentSavings,
            goalAmount = viewModel.currentSavingsGoal,
            progress = progress,
            isGoalReached = isGoalReached,
            remainingAmount = viewModel.getRemainingAmount(),
            onEditGoal = { showGoalEditDialog = true }
        )

        // Thermometer visualization
        Box(
            modifier = Modifier.fillMaxWidth().height(400.dp).padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            ThermometerDisplay(
                progress = animatedProgress,
                isGoalReached = isGoalReached
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Reset button
        ResetDataButton(
            onReset = { showResetDialog = true }
        )

        // Input controls for adding/removing money
        SavingsInputControls(
            inputAmount = inputAmount,
            onInputAmountChange = { inputAmount = it },
            onAddMoney = { amount ->
                viewModel.addSavings(amount)
                inputAmount = ""
            },
            onRemoveMoney = { amount ->
                viewModel.removeSavings(amount)
                inputAmount = ""
            }
        )
    }

    if (viewModel.showCelebration) {
        CelebrationAnimation(
            isVisible = true,
            modifier = Modifier.fillMaxSize(),
            onAnimationComplete = { viewModel.hideCelebration() }
        )
    }

    // Show dialogs when needed
    if (showGoalEditDialog) {
        GoalEditDialog(
            currentGoalName = viewModel.goalName,
            currentGoalAmount = viewModel.currentSavingsGoal,
            onDismiss = { showGoalEditDialog = false },
            onSave = { name, amount ->
                viewModel.updateGoal(amount, name)
                showGoalEditDialog = false
            }
        )
    }

    if (showResetDialog) {
        ResetConfirmationDialog(
            onDismiss = { showResetDialog = false },
            onResetSavings = { viewModel.resetSavingsProgress() },
            onResetAll = { viewModel.resetAllData() }
        )
    }
}