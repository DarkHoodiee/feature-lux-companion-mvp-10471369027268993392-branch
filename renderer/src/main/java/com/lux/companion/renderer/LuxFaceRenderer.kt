package com.lux.companion.renderer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.ClipOp
import com.lux.companion.domain.LuxFaceState
import com.lux.companion.domain.EyeState
import com.lux.companion.domain.StartupPhase

@Composable
fun LuxFaceCanvas(
    state: LuxFaceState,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val masterScale = size.width / 1000f

        withTransform({
            translate(0f, state.verticalOffset * masterScale)
            rotate(state.rotationZ, Offset(centerX, centerY))
        }) {
            // 1. Hardware Base (Visor Depth)
            drawVisorDepth(state, centerX, centerY, masterScale)

            // 2. Content (Startup or Eyes)
            if (state.startupPhase != StartupPhase.ONLINE) {
                drawStartupSequence(state, centerX, centerY, masterScale)
            } else if (state.isRefreshing) {
                drawRefreshEffect(state, centerX, centerY, masterScale)
            } else {
                drawEyes(state, centerX, centerY, masterScale)
            }

            // 3. Hardware Overlays (Raster lines & Scanning)
            drawRasterLines(masterScale)

            if (state.isScanning) {
                drawScanEffect(state.scanProgress, masterScale)
            }
        }
    }
}

private fun DrawScope.drawRasterLines(masterScale: Float) {
    val lineSpacing = 6f * masterScale
    val alpha = 0.06f
    var y = 0f
    while (y < size.height) {
        drawLine(
            color = Color.Black.copy(alpha = alpha),
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = 1.5f * masterScale
        )
        y += lineSpacing
    }
}

private fun DrawScope.drawStartupSequence(state: LuxFaceState, centerX: Float, centerY: Float, masterScale: Float) {
    val progress = state.startupProgress
    val color = Color(0xFF00E5FF)

    when (state.startupPhase) {
        StartupPhase.CENTER_DOT -> {
            drawCircle(color = color, radius = 6f * masterScale, center = Offset(centerX, centerY))
        }
        StartupPhase.HORIZONTAL_EXPANSION -> {
            val lineWidth = size.width * progress
            drawLine(
                color = color,
                start = Offset(centerX - lineWidth / 2f, centerY),
                end = Offset(centerX + lineWidth / 2f, centerY),
                strokeWidth = 3f * masterScale
            )
        }
        StartupPhase.SWEEP_UP_TOP -> {
            val y = centerY - (centerY * progress)
            drawLine(
                color = color,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 2.5f * masterScale
            )
        }
        StartupPhase.NEW_LINE_BOTTOM -> {
            val y = size.height - (size.height * 0.1f * progress)
            drawLine(
                color = color,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 2.5f * masterScale
            )
        }
        StartupPhase.SWEEP_UP_REVEAL -> {
            val scanY = size.height * (1f - progress)
            withTransform({
                clipRect(left = 0f, top = scanY, right = size.width, bottom = size.height, clipOp = ClipOp.Intersect)
            }) {
                drawEyes(state, centerX, centerY, masterScale)
            }
            drawLine(color = color, start = Offset(0f, scanY), end = Offset(size.width, scanY), strokeWidth = 8f * masterScale)
        }
        else -> {}
    }
}

private fun DrawScope.drawVisorDepth(state: LuxFaceState, centerX: Float, centerY: Float, masterScale: Float) {
    val avgLookX = (state.leftEye.lookAtX + state.rightEye.lookAtX) / 2f
    val avgLookY = (state.leftEye.lookAtY + state.rightEye.lookAtY) / 2f

    drawCircle(
        color = Color(0xFF0D47A1).copy(alpha = 0.08f),
        radius = 480f * masterScale,
        center = Offset(centerX + avgLookX * 25f * masterScale, centerY + avgLookY * 12f * masterScale)
    )
}

private fun DrawScope.drawEyes(state: LuxFaceState, centerX: Float, centerY: Float, masterScale: Float) {
    val eyeSpacing = 160f * masterScale
    drawEye(state.leftEye, Offset(centerX - eyeSpacing, centerY), isLeft = true, masterScale = masterScale)
    drawEye(state.rightEye, Offset(centerX + eyeSpacing, centerY), isLeft = false, masterScale = masterScale)
}

private fun DrawScope.drawEye(eyeState: EyeState, center: Offset, isLeft: Boolean, masterScale: Float) {
    val path = EyePathBuilder.buildEyePath(eyeState.topology)
    val lookOffsetX = eyeState.lookAtX * 55f * masterScale
    val lookOffsetY = eyeState.lookAtY * 45f * masterScale

    withTransform({
        translate(center.x + lookOffsetX, center.y + lookOffsetY)
        scale(masterScale, masterScale, Offset.Zero)
        if (isLeft) scale(-1f, 1f, Offset.Zero)
        rotate(eyeState.topology.tilt, Offset.Zero)
        if (eyeState.isBlinking) scale(1f, 1f - eyeState.blinkProgress, Offset.Zero)
    }) {
        val eyeColor = Color(0xFF00A8E8)
        val glow = eyeState.glowIntensity

        drawPath(path = path, color = Color.White.copy(alpha = 0.25f * glow))
        drawPath(path = path, color = eyeColor, alpha = 0.85f * glow)
        drawPath(path = path, color = eyeColor.copy(alpha = 0.4f * glow), style = Stroke(width = 10f))
        drawPath(path = path, color = eyeColor.copy(alpha = 0.18f * glow), style = Stroke(width = 28f))
        drawPath(path = path, color = eyeColor.copy(alpha = 0.06f * glow), style = Stroke(width = 55f))
    }
}

private fun DrawScope.drawScanEffect(progress: Float, masterScale: Float) {
    val y = size.height * progress
    drawLine(color = Color(0x8000E5FF), start = Offset(0f, y), end = Offset(size.width, y), strokeWidth = 4f * masterScale)
}

private fun DrawScope.drawRefreshEffect(state: LuxFaceState, centerX: Float, centerY: Float, masterScale: Float) {
    val progress = state.refreshProgress
    val scanY = size.height * progress
    withTransform({
        clipRect(left = 0f, top = scanY, right = size.width, bottom = size.height, clipOp = ClipOp.Intersect)
    }) {
        drawEyes(state, centerX, centerY, masterScale)
    }
    drawLine(color = Color(0xFF00E5FF), start = Offset(0f, scanY), end = Offset(size.width, scanY), strokeWidth = 12f * masterScale)
}
