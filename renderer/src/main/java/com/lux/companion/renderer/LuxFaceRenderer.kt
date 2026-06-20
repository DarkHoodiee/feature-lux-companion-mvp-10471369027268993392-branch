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
            // Background Visor Parallax Layer (subtle)
            if (state.startupPhase == StartupPhase.COMPLETE) {
                drawVisorDepth(state, centerX, centerY)
            }

            // Eye / Content Layer
            if (state.startupPhase != StartupPhase.COMPLETE && state.startupPhase != StartupPhase.OFF) {
                drawStartupSequence(state, centerX, centerY)
            } else if (state.startupPhase == StartupPhase.COMPLETE) {
                if (state.isRefreshing) {
                    drawRefreshEffect(state, centerX, centerY)
                } else {
                    drawEyes(state, centerX, centerY)
                }

                if (state.isScanning) {
                    drawScanEffect(state.scanProgress)
                }
            }

            // Visor Raster Texture Layer (Drawn LAST to overlay eyes)
            drawVisorTexture()
        }
    }
}

private fun DrawScope.drawVisorTexture() {
    val lineSpacing = 6f
    val lineAlpha = 0.08f
    val lineCount = (size.height / lineSpacing).toInt()

    for (i in 0..lineCount) {
        val y = i * lineSpacing
        drawLine(
            color = Color.Black.copy(alpha = lineAlpha),
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = 1.5f
        )
    }
}

private fun DrawScope.drawStartupSequence(state: LuxFaceState, centerX: Float, centerY: Float) {
    val progress = state.startupProgress
    val color = Color(0xFF00A8E8)

    when (state.startupPhase) {
        StartupPhase.CENTER_DOT -> {
            drawCircle(color, radius = 6f, center = Offset(centerX, centerY))
        }
        StartupPhase.EXPAND_LINE -> {
            val lineWidth = 300f * progress
            drawLine(
                color = color,
                start = Offset(centerX - lineWidth / 2f, centerY),
                end = Offset(centerX + lineWidth / 2f, centerY),
                strokeWidth = 4f
            )
        }
        StartupPhase.SWEEP_UP_DISAPPEAR -> {
            val lineY = centerY - (centerY * progress)
            drawLine(
                color = color,
                start = Offset(0f, lineY),
                end = Offset(size.width, lineY),
                strokeWidth = 6f
            )
        }
        StartupPhase.BOTTOM_LINE_APPEAR -> {
            val lineY = size.height - (size.height / 2f * progress)
            drawLine(
                color = color,
                start = Offset(0f, lineY),
                end = Offset(size.width, lineY),
                strokeWidth = 6f
            )
        }
        StartupPhase.SWEEP_UP_REVEAL -> {
            // Line stays at center while eyes materialize
            val lineY = centerY

            drawEyes(state, centerX, centerY)

            drawLine(
                color = color,
                start = Offset(0f, lineY),
                end = Offset(size.width, lineY),
                strokeWidth = 8f * (1f - progress) // Line fades/shrinks as eyes appear
            )
        }
        else -> {}
    }
}

private fun DrawScope.drawVisorDepth(state: LuxFaceState, centerX: Float, centerY: Float) {
    val avgLookX = (state.leftEye.lookAtX + state.rightEye.lookAtX) / 2f
    val avgLookY = (state.leftEye.lookAtY + state.rightEye.lookAtY) / 2f

    drawCircle(
        color = Color(0xFF0D47A1).copy(alpha = 0.05f),
        radius = 500f,
        center = Offset(centerX + avgLookX * 15f, centerY + avgLookY * 8f)
    )
}

private fun DrawScope.drawEyes(state: LuxFaceState, centerX: Float, centerY: Float) {
    val eyeSpacing = 125f

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
    val geometry = eyeState.geometry
    val path = EyePathBuilder.buildEyePath(geometry, isLeft)

    val lookOffsetX = eyeState.lookAtX * 45f
    val lookOffsetY = eyeState.lookAtY * 35f

    withTransform({
        translate(center.x + lookOffsetX, center.y + lookOffsetY)
        if (eyeState.isBlinking) {
            scale(1f, 1f - eyeState.blinkProgress, Offset.Zero)
        }

        rotate(geometry.tilt * (if (isLeft) 1f else -1f), Offset.Zero)
        scale(eyeState.scaleX, eyeState.scaleY, Offset.Zero)
    }) {
        val eyeColor = Color(0xFF00A8E8) // EVE Cerulean

        drawPath(
            path = path,
            color = eyeColor,
            alpha = 0.85f * eyeState.glowIntensity
        )

        drawPath(
            path = path,
            color = eyeColor.copy(alpha = 0.35f * eyeState.glowIntensity),
            style = Stroke(width = 14f)
        )

        drawPath(
            path = path,
            color = Color.White.copy(alpha = 0.15f * eyeState.glowIntensity)
        )
    }
}

private fun DrawScope.drawScanEffect(progress: Float) {
    val y = size.height * progress
    drawLine(
        color = Color(0x6000A8E8),
        start = Offset(0f, y),
        end = Offset(size.width, y),
        strokeWidth = 3f
    )
}

private fun DrawScope.drawRefreshEffect(state: LuxFaceState, centerX: Float, centerY: Float) {
    val progress = state.refreshProgress
    val scanY = size.height * progress

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
        color = Color(0xFF00A8E8),
        start = Offset(0f, scanY),
        end = Offset(size.width, scanY),
        strokeWidth = 10f
    )
}
