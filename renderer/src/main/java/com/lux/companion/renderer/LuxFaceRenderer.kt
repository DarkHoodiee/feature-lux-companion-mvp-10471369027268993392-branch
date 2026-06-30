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

        withTransform({
            translate(0f, state.verticalOffset)
            rotate(state.rotationZ, Offset(centerX, centerY))
        }) {
            drawVisorDepth(state, centerX, centerY)

            if (state.startupPhase != StartupPhase.ONLINE && state.startupPhase != StartupPhase.OFF) {
                drawStartupPhase(state, centerX, centerY)
            } else if (state.startupPhase == StartupPhase.ONLINE) {
                if (state.isRefreshing) {
                    drawRefreshEffect(state, centerX, centerY)
                } else {
                    drawEyes(state, centerX, centerY)
                }

                if (state.isScanning) {
                    drawScanEffect(state.scanProgress)
                }

                // Overlay subtle raster lines
                drawVisorRasterLines()
            }
        }
    }
}

private fun DrawScope.drawVisorDepth(state: LuxFaceState, centerX: Float, centerY: Float) {
    val avgLookX = (state.leftEye.lookAtX + state.rightEye.lookAtX) / 2f
    val avgLookY = (state.leftEye.lookAtY + state.rightEye.lookAtY) / 2f

    drawCircle(
        color = Color(0xFF0D47A1).copy(alpha = 0.08f),
        radius = 450f,
        center = Offset(centerX + avgLookX * 12f, centerY + avgLookY * 6f)
    )
}

private fun DrawScope.drawVisorRasterLines() {
    val spacing = 8f
    val count = (size.height / spacing).toInt()
    for (i in 0..count) {
        val y = i * spacing
        drawLine(
            color = Color.Black.copy(alpha = 0.05f),
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = 1f
        )
    }
}

private fun DrawScope.drawStartupPhase(state: LuxFaceState, centerX: Float, centerY: Float) {
    val p = state.startupProgress
    val color = Color(0xFF00E5FF)

    when (state.startupPhase) {
        StartupPhase.BOOT_DOT -> {
            drawCircle(color = color, radius = 4f, center = Offset(centerX, centerY))
        }
        StartupPhase.EXPANSION_LINE -> {
            val width = 400f * p
            drawLine(
                color = color,
                start = Offset(centerX - width / 2f, centerY),
                end = Offset(centerX + width / 2f, centerY),
                strokeWidth = 2f
            )
        }
        StartupPhase.UPPER_SWEEP -> {
            val y = centerY - (centerY * p)
            drawLine(
                color = color.copy(alpha = 1f - p),
                start = Offset(centerX - 200f, y),
                end = Offset(centerX + 200f, y),
                strokeWidth = 2f
            )
        }
        StartupPhase.LOWER_SWEEP -> {
            val y = size.height - (size.height / 2f * p)
            drawLine(
                color = color,
                start = Offset(centerX - 200f, y),
                end = Offset(centerX + 200f, y),
                strokeWidth = 2f
            )
        }
        StartupPhase.EYE_MATERIALIZING -> {
            drawEyes(state, centerX, centerY)
        }
        else -> {}
    }
}

private fun DrawScope.drawEyes(state: LuxFaceState, centerX: Float, centerY: Float) {
    val eyeSpacing = 120f

    drawEye(
        state.leftEye,
        Offset(centerX - eyeSpacing, centerY),
        isLeft = true
    )

    drawEye(
        state.rightEye,
        Offset(centerX + eyeSpacing, centerY),
        isLeft = false
    )
}

private fun DrawScope.drawEye(eyeState: EyeState, center: Offset, isLeft: Boolean) {
    val topology = eyeState.topology
    val baseWidth = 100f * topology.widthScale
    val baseHeight = 100f * topology.heightScale

    val path = EyePathBuilder.buildEyePath(
        topology = topology,
        width = baseWidth,
        height = baseHeight,
        side = if (isLeft) EyeSide.Left else EyeSide.Right
    )

    val lookOffsetX = eyeState.lookAtX * 40f
    val lookOffsetY = eyeState.lookAtY * 30f

    withTransform({
        translate(center.x + lookOffsetX, center.y + lookOffsetY)

        if (eyeState.isBlinking) {
            scale(1f, 1f - eyeState.blinkProgress, Offset.Zero)
        }
    }) {
        val eyeColor = Color(0xFF00A8E8) // Canonical Lux Cerulean

        // Core
        drawPath(
            path = path,
            color = Color.White.copy(alpha = 0.2f * eyeState.glowIntensity)
        )

        // Emissive
        drawPath(
            path = path,
            color = eyeColor,
            alpha = 0.9f * eyeState.glowIntensity
        )

        // Bloom
        drawPath(
            path = path,
            color = eyeColor.copy(alpha = 0.4f * eyeState.glowIntensity),
            style = Stroke(width = 12f)
        )

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
