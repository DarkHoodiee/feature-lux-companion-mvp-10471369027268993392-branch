package com.lux.hoodie.renderer

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

@Composable
fun StartupSequence(
    onComplete: () -> Unit
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 4000, easing = LinearEasing)
        )
        onComplete()
    }

    val p = progress.value

    Canvas(modifier = Modifier.fillMaxSize().background(VisorBlack)) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f

        when {
            p < 0.1f -> {
                // OFF
            }
            p < 0.2f -> {
                // BOOT_DOT
                val dotAlpha = (p - 0.1f) / 0.1f
                drawCircle(LuxCerulean.copy(alpha = dotAlpha), radius = 8f, center = Offset(centerX, centerY))
            }
            p < 0.4f -> {
                // EXPANSION_LINE
                val lineProgress = (p - 0.2f) / 0.2f
                val lineWidth = size.width * 0.7f * lineProgress
                drawRect(
                    color = LuxCerulean,
                    topLeft = Offset(centerX - lineWidth / 2f, centerY - 2f),
                    size = Size(lineWidth, 4f)
                )
            }
            p < 0.6f -> {
                // UPPER_SWEEP
                val sweepProgress = (p - 0.4f) / 0.2f
                val lineWidth = size.width * 0.7f
                val sweepHeight = centerY * sweepProgress
                drawRect(
                    color = LuxCerulean.copy(alpha = 0.3f),
                    topLeft = Offset(centerX - lineWidth / 2f, centerY - sweepHeight),
                    size = Size(lineWidth, sweepHeight)
                )
                // Keep the center line
                drawRect(
                    color = LuxCerulean,
                    topLeft = Offset(centerX - lineWidth / 2f, centerY - 2f),
                    size = Size(lineWidth, 4f)
                )
            }
            p < 0.8f -> {
                // LOWER_SWEEP
                val sweepProgress = (p - 0.6f) / 0.2f
                val lineWidth = size.width * 0.7f
                val sweepHeight = centerY * sweepProgress

                // Draw upper sweep (full)
                drawRect(
                    color = LuxCerulean.copy(alpha = 0.3f),
                    topLeft = Offset(centerX - lineWidth / 2f, 0f),
                    size = Size(lineWidth, centerY)
                )

                // Draw lower sweep
                drawRect(
                    color = LuxCerulean.copy(alpha = 0.3f),
                    topLeft = Offset(centerX - lineWidth / 2f, centerY),
                    size = Size(lineWidth, sweepHeight)
                )

                // Keep the center line
                drawRect(
                    color = LuxCerulean,
                    topLeft = Offset(centerX - lineWidth / 2f, centerY - 2f),
                    size = Size(lineWidth, 4f)
                )
            }
            else -> {
                // EYE_MATERIALIZING
                val matProgress = (p - 0.8f) / 0.2f
                val alpha = matProgress

                // Fade out the sweeps and fade in the eyes (placeholder for actual eyes)
                drawRect(
                    color = LuxCerulean.copy(alpha = 0.3f * (1f - matProgress)),
                    topLeft = Offset(centerX - size.width * 0.35f, 0f),
                    size = Size(size.width * 0.7f, size.height)
                )
            }
        }
    }
}
