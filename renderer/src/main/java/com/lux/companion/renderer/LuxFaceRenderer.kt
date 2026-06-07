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
    )

    // Right Eye
    drawEye(
        eyeState,
        Offset(centerX + eyeSpacing - eyeWidth / 2f + lookOffsetX, centerY + lookOffsetY),
        Size(eyeWidth, eyeHeight)
    )
}

private fun DrawScope.drawEye(eyeState: EyeState, topLeft: Offset, size: Size) {
    val path = EyePathBuilder.buildPath(eyeState.topology)

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
    )
}
