
package com.example.save_o_meter

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.*
import kotlin.random.Random

/**
 * Data class representing a single particle (sparkle/firework)
 */
data class Particle(
    val startX: Float,
    val startY: Float,
    val velocityX: Float,
    val velocityY: Float,
    val color: Color,
    val size: Float,
    val life: Float = 1f
)

/**
 * Celebration animation with sparkles and fireworks
 */
@Composable
fun CelebrationAnimation(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    onAnimationComplete: () -> Unit = {}
) {
    var particles by remember { mutableStateOf(listOf<Particle>()) }
    var animationTime by remember { mutableStateOf(0f) }

    // Animation state
    val infiniteTransition = rememberInfiniteTransition(label = "celebration")
    val animatedTime by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )

    // Create particles when animation starts
    LaunchedEffect(isVisible) {
        if (isVisible) {
            particles = generateParticles()
            animationTime = 0f

            // Auto-hide after 4 seconds
            delay(4000)
            onAnimationComplete()
        }
    }

    // Update animation time
    LaunchedEffect(animatedTime, isVisible) {
        if (isVisible) {
            animationTime += 0.1f
        }
    }

    if (isVisible) {
        Box(modifier = modifier.fillMaxSize()) {
            // Particle system
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawParticles(particles, animationTime)
            }

            // Congratulations message
            CelebrationMessage(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

/**
 * Generate initial particles for the explosion effect
 */
private fun generateParticles(): List<Particle> {
    val particles = mutableListOf<Particle>()
    val colors = listOf(
        Color(0xFFFFD700), // Gold
        Color(0xFFFF6B35), // Orange
        Color(0xFFFF1744), // Red
        Color(0xFF4CAF50), // Green
        Color(0xFF2196F3), // Blue
        Color(0xFF9C27B0), // Purple
        Color(0xFFFFFFFF)  // White
    )

    // Create multiple bursts
    repeat(3) { burstIndex ->
        val centerX = 200f + (burstIndex * 100f)
        val centerY = 300f + (burstIndex * 50f)

        // Create particles in each burst
        repeat(15) {
            val angle = Random.nextFloat() * 2 * PI
            val speed = Random.nextFloat() * 200f + 50f

            particles.add(
                Particle(
                    startX = centerX,
                    startY = centerY,
                    velocityX = cos(angle).toFloat() * speed,
                    velocityY = sin(angle).toFloat() * speed,
                    color = colors.random(),
                    size = Random.nextFloat() * 8f + 4f
                )
            )
        }
    }

    return particles
}

/**
 * Draw all particles with physics simulation
 */
private fun DrawScope.drawParticles(particles: List<Particle>, time: Float) {
    particles.forEach { particle ->
        // Physics simulation
        val gravity = 98f // Gravity acceleration
        val x = particle.startX + particle.velocityX * time
        val y = particle.startY + particle.velocityY * time + 0.5f * gravity * time * time

        // Fade out over time
        val alpha = maxOf(0f, 1f - time * 0.5f)
        val currentSize = particle.size * (1f - time * 0.3f)

        if (alpha > 0 && currentSize > 0) {
            drawCircle(
                color = particle.color.copy(alpha = alpha),
                radius = currentSize,
                center = Offset(x, y)
            )

            // Add sparkle effect
            if (Random.nextFloat() < 0.3f) {
                drawStar(
                    center = Offset(x, y),
                    color = particle.color.copy(alpha = alpha * 0.7f),
                    size = currentSize * 0.5f
                )
            }
        }
    }
}

/**
 * Draw a simple star shape for sparkle effect
 */
private fun DrawScope.drawStar(center: Offset, color: Color, size: Float) {
    val points = 5
    val outerRadius = size
    val innerRadius = size * 0.4f

    val path = androidx.compose.ui.graphics.Path()

    for (i in 0 until points * 2) {
        val angle = (i * PI / points).toFloat()
        val radius = if (i % 2 == 0) outerRadius else innerRadius
        val x = center.x + cos(angle) * radius
        val y = center.y + sin(angle) * radius

        if (i == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
    }
    path.close()

    drawPath(path, color)
}

/**
 * Animated congratulations message
 */
@Composable
private fun CelebrationMessage(modifier: Modifier = Modifier) {
    val scale by rememberInfiniteTransition(label = "scale").animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "message_scale"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🎉 GOAL REACHED! 🎉",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFD700), // Gold color
            textAlign = TextAlign.Center,
            modifier = Modifier.graphicsLayer(scaleX = scale, scaleY = scale)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Congratulations on reaching your savings goal!",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .graphicsLayer(alpha = 0.9f)
        )
    }
}