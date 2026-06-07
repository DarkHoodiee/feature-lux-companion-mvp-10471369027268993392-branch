package com.lux.companion.renderer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.ClipOp
import com.lux.companion.domain.LuxFaceState
import com.lux.companion.domain.EyeState

@Composable
fun LuxFaceCanvas(
    state: LuxFaceState,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f

        withTransform({
            translate(0f, state.verticalOffset)
            rotate(state.rotationZ, Offset(centerX, centerY))
        }) {
            if (!state.isRefreshing) {
                drawEyes(state.eyeState, centerX, centerY)

            // Background Visor Parallax Layer (subtle)
            drawVisorDepth(state, centerX, centerY)

            if (state.isRefreshing) {
                drawRefreshEffect(state, centerX, centerY)
            } else {
                drawEyes(state, centerX, centerY)
            }

            if (state.isScanning) {
                drawScanEffect(state.scanProgress)
            }

            if (state.isRefreshing) {
                drawRefreshEffect(state.refreshProgress)
            }
        }
    }
}

private fun DrawScope.drawEyes(eyeState: EyeState, centerX: Float, centerY: Float) {
    val eyeWidth = 120f * eyeState.scaleX
    val eyeHeight = 150f * eyeState.scaleY
    val eyeSpacing = 100f

    val lookOffsetX = eyeState.lookAtX * 30f
    val lookOffsetY = eyeState.lookAtY * 20f

    // Left Eye
    drawEye(
        eyeState,
        Offset(centerX - eyeSpacing - eyeWidth / 2f + lookOffsetX, centerY + lookOffsetY),
        Size(eyeWidth, eyeHeight)
        }
    }
}

private fun DrawScope.drawVisorDepth(state: LuxFaceState, centerX: Float, centerY: Float) {
    // Subtle gradient or glow that moves with look-at but with more "lag" to simulate depth
    val avgLookX = (state.leftEye.lookAtX + state.rightEye.lookAtX) / 2f
    val avgLookY = (state.leftEye.lookAtY + state.rightEye.lookAtY) / 2f

    drawCircle(
        color = Color(0xFF0D47A1).copy(alpha = 0.1f),
        radius = 400f,
        center = Offset(centerX + avgLookX * 10f, centerY + avgLookY * 5f)
    )
}

private fun DrawScope.drawEyes(state: LuxFaceState, centerX: Float, centerY: Float) {
    val eyeSpacing = 120f

    // Left Eye
    drawEye(
        state.leftEye,
        Offset(centerX - eyeSpacing, centerY),
        isLeft = true
    )

    // Right Eye
    drawEye(
        eyeState,
        Offset(centerX + eyeSpacing - eyeWidth / 2f + lookOffsetX, centerY + lookOffsetY),
        Size(eyeWidth, eyeHeight)
    )
}

private fun DrawScope.drawEye(eyeState: EyeState, topLeft: Offset, size: Size) {
    val path = EyePathProvider.getEyePath(eyeState.expression, size)

    withTransform({
        translate(topLeft.x, topLeft.y)
        if (eyeState.isBlinking) {
            scale(1f, 1f - eyeState.blinkProgress, Offset(size.width / 2f, size.height / 2f))
        }
    }) {
        // Drawing with glow
        val color = Color(0xFF81D4FA) // Light Blue
        drawPath(
            path = path,
            color = color,
            alpha = eyeState.glowIntensity
        )

        // Add subtle glow layer
        drawPath(
            path = path,
            color = color.copy(alpha = 0.3f),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 10f)
        state.rightEye,
        Offset(centerX + eyeSpacing, centerY),
        isLeft = false
    )
}

private fun DrawScope.drawEye(eyeState: EyeState, center: Offset, isLeft: Boolean) {
    val geometry = eyeState.geometry
    val path = EyePathBuilder.buildEyePath(geometry)

    val lookOffsetX = eyeState.lookAtX * 40f
    val lookOffsetY = eyeState.lookAtY * 30f

    withTransform({
        translate(center.x + lookOffsetX, center.y + lookOffsetY)
        rotate(geometry.tilt * (if (isLeft) 1f else -1f), Offset.Zero)

        if (eyeState.isBlinking) {
            scale(1f, 1f - eyeState.blinkProgress, Offset.Zero)
        }
    }) {
        val eyeColor = Color(0xFF81D4FA) // Luminous Cerulean

        // Inner luminous core
        drawPath(
            path = path,
            color = Color.White.copy(alpha = 0.2f * eyeState.glowIntensity)
        )

        // Main emissive color
        drawPath(
            path = path,
            color = eyeColor,
            alpha = 0.9f * eyeState.glowIntensity
        )

        // Bloom / Glow Border
        drawPath(
            path = path,
            color = eyeColor.copy(alpha = 0.4f * eyeState.glowIntensity),
            style = Stroke(width = 12f)
        )

        // Secondary outer glow
        drawPath(
            path = path,
            color = eyeColor.copy(alpha = 0.15f * eyeState.glowIntensity),
            style = Stroke(width = 24f)
        )
    }
}

private fun DrawScope.drawScanEffect(progress: Float) {
    val y = size.height * progress
    drawLine(
        color = Color(0x8000E5FF),
        start = Offset(0f, y),
        end = Offset(size.width, y),
        strokeWidth = 4f
    )
}

private fun DrawScope.drawRefreshEffect(progress: Float) {
    val color = Color(0xFF00E5FF)
    val lineY = size.height * progress
    drawLine(
        color = color,
        start = Offset(0f, lineY),
        end = Offset(size.width, lineY),
        strokeWidth = 8f
        strokeWidth = 6f
    )
}

private fun DrawScope.drawRefreshEffect(state: LuxFaceState, centerX: Float, centerY: Float) {
    val progress = state.refreshProgress
    val scanY = size.height * progress

    withTransform({
        clipRect(
            left = 0f,
            top = scanY,
            right = size.width,
            bottom = size.height,
            clipOp = ClipOp.Intersect
        )
    }) {
        drawEyes(state, centerX, centerY)
    }

    drawLine(
        color = Color(0xFF00E5FF),
        start = Offset(0f, scanY),
        end = Offset(size.width, scanY),
        strokeWidth = 10f
    )
}
