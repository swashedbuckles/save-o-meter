package com.example.save_o_meter

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.min

/**
 * Draws the thermometer visualization
 */
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

    // Draw thermometer outline
    drawRoundRect(
        color = Color.LightGray,
        topLeft = Offset(thermometerLeft, thermometerTop),
        size = Size(thermometerWidth, thermometerHeight),
        cornerRadius = CornerRadius(thermometerWidth / 2, thermometerWidth / 2)
    )

    // Draw bulb outline
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

    // Determine mercury color based on progress
    val mercuryColor = when {
        isGoalReached -> Color.Red
        progress > 0.8f -> Color(0xFFFF6B35)
        progress > 0.6f -> Color(0xFFFF8F00)
        progress > 0.4f -> Color(0xFFFFC107)
        progress > 0.2f -> Color(0xFF8BC34A)
        else -> Color(0xFF4CAF50)
    }

    // Fill thermometer with progress
    if (progress > 0) {
        if (fillHeight > innerPadding) {
            drawRoundRect(
                color = mercuryColor,
                topLeft = Offset(innerLeft, max(fillTop, thermometerTop + innerPadding)),
                size = Size(innerWidth, min(fillHeight, thermometerHeight - innerPadding * 2)),
                cornerRadius = CornerRadius(innerWidth / 2, innerWidth / 2)
            )
        }

        // Fill bulb
        val innerBulbRadius = bulbRadius - innerPadding
        drawCircle(
            color = mercuryColor,
            radius = innerBulbRadius,
            center = Offset(bulbCenterX, bulbCenterY)
        )
    }

    // Add thermometer tick marks
    for (i in 0..10) {
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
        // Draw pulsing glow around bulb
        drawCircle(
            color = Color(0xFFFFD700).copy(alpha = 0.3f), // Gold glow
            radius = bulbRadius + 10.dp.toPx(),
            center = Offset(bulbCenterX, bulbCenterY)
        )

        // Draw sparkles around thermometer
        drawSparklesAroundThermometer(thermometerLeft, thermometerTop, thermometerWidth, thermometerHeight)
    }
}

/**
 * Draw sparkles around the thermometer when goal is reached
 */
private fun DrawScope.drawSparklesAroundThermometer(
    thermometerLeft: Float,
    thermometerTop: Float,
    thermometerWidth: Float,
    thermometerHeight: Float
) {
    val sparklePositions = listOf(
        Offset(thermometerLeft - 20.dp.toPx(), thermometerTop + 50.dp.toPx()),
        Offset(thermometerLeft + thermometerWidth + 20.dp.toPx(), thermometerTop + 80.dp.toPx()),
        Offset(thermometerLeft - 15.dp.toPx(), thermometerTop + 150.dp.toPx()),
        Offset(thermometerLeft + thermometerWidth + 25.dp.toPx(), thermometerTop + 180.dp.toPx()),
        Offset(thermometerLeft - 10.dp.toPx(), thermometerTop + thermometerHeight - 50.dp.toPx()),
        Offset(thermometerLeft + thermometerWidth + 15.dp.toPx(), thermometerTop + thermometerHeight - 20.dp.toPx())
    )

    sparklePositions.forEach { position ->
        drawStar(
            center = position,
            color = Color(0xFFFFD700), // Gold
            size = 8.dp.toPx()
        )
    }
}

/**
 * Draw a simple star shape
 */
private fun DrawScope.drawStar(center: Offset, color: Color, size: Float) {
    // Draw a simple 4-pointed star (plus sign rotated)
    drawLine(
        color = color,
        start = Offset(center.x - size, center.y),
        end = Offset(center.x + size, center.y),
        strokeWidth = 3.dp.toPx(),
        cap = StrokeCap.Round
    )
    drawLine(
        color = color,
        start = Offset(center.x, center.y - size),
        end = Offset(center.x, center.y + size),
        strokeWidth = 3.dp.toPx(),
        cap = StrokeCap.Round
    )

    // Add diagonal lines for 8-pointed star
    val diagonal = size * 0.7f
    drawLine(
        color = color,
        start = Offset(center.x - diagonal, center.y - diagonal),
        end = Offset(center.x + diagonal, center.y + diagonal),
        strokeWidth = 2.dp.toPx(),
        cap = StrokeCap.Round
    )
    drawLine(
        color = color,
        start = Offset(center.x - diagonal, center.y + diagonal),
        end = Offset(center.x + diagonal, center.y - diagonal),
        strokeWidth = 2.dp.toPx(),
        cap = StrokeCap.Round
    )
}

/**
 * Displays the thermometer visualization
 */
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