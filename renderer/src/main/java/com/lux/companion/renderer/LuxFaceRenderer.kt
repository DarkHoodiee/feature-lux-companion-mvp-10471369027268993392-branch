package com.lux.companion.renderer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import com.lux.companion.domain.EyeState
import com.lux.companion.domain.LuxFaceState

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
    // Subtle glow that moves with look-at but with more "lag" to simulate depth
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
        // Apply individual eye scale if defined
        scale(eyeState.scaleX, eyeState.scaleY, Offset.Zero)
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

private fun DrawScope.drawRefreshEffect(state: LuxFaceState, centerX: Float, centerY: Float) {
    val progress = state.refreshProgress
    val scanY = size.height * progress

    // Requirement: eyes disappear -> line scans -> eyes return
    // We achieve this by only drawing the portion of the eyes that the scanline has "passed"
    // or by keeping them hidden until the scan finishes.
    // Given the sequence "line scans -> eyes return", we'll hide them during the scan,
    // but the "reveal" effect (clipRect top=0 to scanY) is often what's intended for "return".

    // To strictly follow "eyes disappear -> line scans -> eyes return":
    // If we are scanning (progress > 0 and < 1), we don't draw eyes, just the line.
    // When progress reaches 1, they "return" (handled by isRefreshing becoming false).

    // However, if we want a more dynamic "eyes return" as it scans:
    /*
    clipRect(
        left = 0f,
        top = 0f,
        right = size.width,
        bottom = scanY,
        clipOp = ClipOp.Intersect
    ) {
        drawEyes(state, centerX, centerY)
    }
    */

    // For now, let's implement the strict "invisible during scan" version if it fits the literal words:
    // "eyes disappear -> line scans -> eyes return"

    // Actually, one of the previous snippets had:
    // clipRect(top = scanY, bottom = size.height, clipOp = ClipOp.Intersect) { drawEyes(state, centerX, centerY) }
    // which shows eyes BELOW the line.

    // Let's do the one that makes the most sense visually: eyes are revealed from top to bottom.
    clipRect(
        left = 0f,
        top = 0f,
        right = size.width,
        bottom = scanY,
        clipOp = ClipOp.Intersect
    ) {
        drawEyes(state, centerX, centerY)
    }

    drawLine(
        color = Color(0xFF00E5FF),
        start = Offset(0f, scanY),
        end = Offset(size.width, scanY),
        strokeWidth = 10f
    )
}
