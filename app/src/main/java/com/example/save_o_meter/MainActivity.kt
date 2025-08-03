package com.example.save_o_meter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.save_o_meter.ui.theme.SaveoMeterTheme
import java.util.UUID
import kotlin.math.max
import kotlin.math.min

data class SavingsGoal (
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val targetAmount: Float,
    val currentAmount: Float = 0f
)
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

private fun DrawScope.drawThermometer(
    progress: Float,
    isGoalReached: Boolean,
    bulbScale: Float
) {
    val thermometerWidth = 40.dp.toPx()
    val thermometerHeight = 250.dp.toPx()
    val bulbRadius = 30.dp.toPx() * bulbScale

    val thermometerLeft = (size.width - thermometerWidth) / 2
    val thermometerTop = 20.dp.toPx()
    val bulbCenterY = thermometerTop + thermometerHeight + bulbRadius - 10.dp.toPx()
    val bulbCenterX = size.width / 2

    drawRoundRect(
        color = Color.LightGray,
        topLeft = Offset(thermometerLeft, thermometerTop),
        size = Size(thermometerWidth, thermometerHeight),
        cornerRadius = CornerRadius(thermometerWidth / 2, thermometerWidth / 2)
    )

    drawCircle(
        color = Color.LightGray,
        radius = bulbRadius,
        center = Offset(bulbCenterX, bulbCenterY)
    )

    val fillHeight = thermometerHeight * progress
    val fillTop = thermometerTop + thermometerHeight - fillHeight
    val innerPadding = 4.dp.toPx()
    val innerWidth = thermometerWidth - (innerPadding * 2)
    val innerLeft = thermometerLeft + innerPadding

    val mercuryColor = when {
        isGoalReached -> Color.Red
        progress > 0.8f -> Color (0xFFFF6B35)
        progress > 0.6f -> Color (0xFFFF8F00)
        progress > 0.4f -> Color (0xFFFFC107)
        progress > 0.2f ->  Color (0xFF8BC34A)
        else -> Color(0xFF4CAF50)
    }

    // fill thermometer with progress
    if (progress > 0) {
        if(fillHeight > innerPadding) {
            drawRoundRect(
                color = mercuryColor,
                topLeft = Offset(innerLeft, max(fillTop, thermometerTop + innerPadding)),
                size = Size(innerWidth, min(fillHeight, thermometerHeight - innerPadding * 2)),
                cornerRadius = CornerRadius(innerWidth / 2, innerWidth / 2)
            )
        }

        val innerBulbRadius = bulbRadius - innerPadding
        drawCircle(
            color= mercuryColor,
            radius = innerBulbRadius,
            center = Offset(bulbCenterX, bulbCenterY)
        )
    }

    // add thermometer demarcations
    for(i in 0..10) {
         val markHeight = thermometerTop + (thermometerHeight * i / 10)
         drawLine(
             color = Color.Gray,
             start = Offset(thermometerLeft + thermometerWidth + 5.dp.toPx(), markHeight),
             end = Offset(thermometerLeft + thermometerWidth + 15.dp.toPx(), markHeight),
             strokeWidth = 2.dp.toPx(),
             cap = StrokeCap.Round
         )
     }

    if (isGoalReached) {
        // do something
    }
}

@Composable
fun ThermometerDisplay(
    progress: Float,
    isGoalReached: Boolean,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(width = 120.dp, height = 350.dp)) {
        drawThermometer(progress, isGoalReached, bulbScale = 1f)
    }
}

@Composable
fun SavingsThermometerScreen(modifier: Modifier = Modifier) {
    var currentSavings by remember { mutableFloatStateOf(0f) }
    var currentSavingsGoal by remember { mutableFloatStateOf(0f) }
    var inputAmount by remember { mutableStateOf("") }

    val progress = if (currentSavingsGoal > 0) currentSavings / currentSavingsGoal else 0f
    val isGoalReached = progress >= 1f

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Savings Thermometer",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Goal $${currentSavingsGoal.toInt()}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Saved: $${currentSavings.toInt()}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${(progress * 100).toInt()}% Complete",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isGoalReached) Color.Red else MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth().height(400.dp).padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            ThermometerDisplay(
                progress = 1.0f,
                isGoalReached = isGoalReached
            )
        }
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SaveoMeterTheme {
        Greeting("Android")
    }
}