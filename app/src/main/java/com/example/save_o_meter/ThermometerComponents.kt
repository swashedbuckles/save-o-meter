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
 * Draws the thermometer visualization using the full canvas size
 */
private fun DrawScope.drawThermometer(
    progress: Float,
    isGoalReached: Boolean,
    bulbScale: Float
) {

    val canvasWidth = size.width
    val canvasHeight = size.height

    // Calculate thermometer dimensions as percentages of canvas
    val thermometerWidth = canvasWidth * 0.33f // 20% of canvas width
    val bulbRadius = thermometerWidth * 0.75f // Bulb is 75% of thermometer width
    val thermometerHeight = canvasHeight - (bulbRadius * 2) - (0.02f * canvasHeight)

    // Center the thermometer horizontally
    val thermometerLeft = (canvasWidth - thermometerWidth) / 2
    val thermometerTop = canvasHeight * 0.05f // Start 5% from top
    val bulbCenterY = thermometerTop + thermometerHeight + bulbRadius * 0.5f
    val bulbCenterX = canvasWidth / 2

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
        radius = bulbRadius * bulbScale,
        center = Offset(bulbCenterX, bulbCenterY)
    )

    val fillHeight = thermometerHeight * progress
    val fillTop = thermometerTop + thermometerHeight - fillHeight
    val innerPadding = thermometerWidth * 0.1f // 10% of width as padding
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
        val innerBulbRadius = bulbRadius * bulbScale - innerPadding
        drawCircle(
            color = mercuryColor,
            radius = innerBulbRadius,
            center = Offset(bulbCenterX, bulbCenterY)
        )
    }

    // Add thermometer tick marks (responsive sizing)
    val tickLength = thermometerWidth * 0.3f
    val tickOffset = thermometerWidth * 0.1f
    val tickWidth = max(2f, thermometerWidth * 0.05f)

    for (i in 0..10) {
        val markHeight = thermometerTop + (thermometerHeight * i / 10)
        if(i > 0 && i < 10) {
            drawLine(
                color = Color.Gray,
                start = Offset(thermometerLeft + tickOffset, markHeight),
                end = Offset(thermometerLeft  + tickOffset + tickLength, markHeight),
                strokeWidth = tickWidth,
                cap = StrokeCap.Round
            )
        }
    }

    // Goal reached effects (responsive sizing)
    if (isGoalReached) {
        // Draw pulsing glow around bulb
        drawCircle(
            color = Color(0xFFFFD700).copy(alpha = 0.3f),
            radius = bulbRadius * bulbScale + thermometerWidth * 0.2f,
            center = Offset(bulbCenterX, bulbCenterY)
        )

        // Draw sparkles around thermometer
        drawSparklesAroundThermometer(
            thermometerLeft,
            thermometerTop,
            thermometerWidth,
            thermometerHeight,
            canvasWidth,
            canvasHeight
        )
    }
}

/**
 * Draw sparkles around the thermometer (responsive positioning)
 */
private fun DrawScope.drawSparklesAroundThermometer(
    thermometerLeft: Float,
    thermometerTop: Float,
    thermometerWidth: Float,
    thermometerHeight: Float,
    canvasWidth: Float,
    canvasHeight: Float
) {
    val sparkleDistance = thermometerWidth * 0.8f
    val sparkleSize = thermometerWidth * 0.15f

    val sparklePositions = listOf(
        Offset(thermometerLeft - sparkleDistance, thermometerTop + thermometerHeight * 0.2f),
        Offset(thermometerLeft + thermometerWidth + sparkleDistance, thermometerTop + thermometerHeight * 0.3f),
        Offset(thermometerLeft - sparkleDistance * 0.7f, thermometerTop + thermometerHeight * 0.6f),
        Offset(thermometerLeft + thermometerWidth + sparkleDistance * 0.9f, thermometerTop + thermometerHeight * 0.7f),
        Offset(thermometerLeft - sparkleDistance * 0.5f, thermometerTop + thermometerHeight * 0.9f),
        Offset(thermometerLeft + thermometerWidth + sparkleDistance * 0.6f, thermometerTop + thermometerHeight * 0.95f)
    )

    sparklePositions.forEach { position ->
        // Only draw sparkles that are within canvas bounds
        if (position.x > 0 && position.x < canvasWidth &&
            position.y > 0 && position.y < canvasHeight) {
            drawStar(
                center = position,
                color = Color(0xFFFFD700),
                size = sparkleSize
            )
        }
    }
}

/**
 * Draw a responsive star shape
 */
private fun DrawScope.drawStar(center: Offset, color: Color, size: Float) {
    val strokeWidth = max(2f, size * 0.2f)

    // Draw a simple 4-pointed star (plus sign rotated)
    drawLine(
        color = color,
        start = Offset(center.x - size, center.y),
        end = Offset(center.x + size, center.y),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round
    )
    drawLine(
        color = color,
        start = Offset(center.x, center.y - size),
        end = Offset(center.x, center.y + size),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round
    )

    // Add diagonal lines for 8-pointed star
    val diagonal = size * 0.7f
    val diagonalStroke = strokeWidth * 0.7f
    drawLine(
        color = color,
        start = Offset(center.x - diagonal, center.y - diagonal),
        end = Offset(center.x + diagonal, center.y + diagonal),
        strokeWidth = diagonalStroke,
        cap = StrokeCap.Round
    )
    drawLine(
        color = color,
        start = Offset(center.x - diagonal, center.y + diagonal),
        end = Offset(center.x + diagonal, center.y - diagonal),
        strokeWidth = diagonalStroke,
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
    Canvas(modifier = modifier) {
        drawThermometer(progress, isGoalReached, bulbScale = 1.25f)
    }
}