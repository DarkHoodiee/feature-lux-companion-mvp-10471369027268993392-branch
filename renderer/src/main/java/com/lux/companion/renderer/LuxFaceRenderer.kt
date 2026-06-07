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

            // Handle the Refresh effect (Scanline travels vertically and masks the face)
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

        // Individual eye tilt (Curious, etc.)
        // Left eye usually tilts inward for curiosity, right eye outward.
        // The tilt parameter in EyeGeometry handles the specific angle.
        rotate(geometry.tilt * (if (isLeft) 1f else -1f), Offset.Zero)

        if (eyeState.isBlinking) {
            scale(1f, 1f - eyeState.blinkProgress, Offset.Zero)
        }
    }) {
        val color = Color(0xFF81D4FA) // Light Blue

        // Main eye fill
        drawPath(
            path = path,
            color = color,
            alpha = eyeState.glowIntensity
        )

        // Subtle glow border
        drawPath(
            path = path,
            color = color.copy(alpha = 0.4f),
            style = Stroke(width = 8f)
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

    // In EVE, the refresh line usually "reveals" the eyes as it moves.
    // Here we clip the drawing area based on the scanline position.

    withTransform({
        // Only draw eyes BELOW the scanline to simulate a "refreshing reveal"
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

    // Draw the bright refresh line itself
    drawLine(
        color = Color(0xFF00E5FF),
        start = Offset(0f, scanY),
        end = Offset(size.width, scanY),
        strokeWidth = 10f
    )
}
