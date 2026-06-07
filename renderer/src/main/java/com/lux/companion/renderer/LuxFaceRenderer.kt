package com.lux.companion.renderer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
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
