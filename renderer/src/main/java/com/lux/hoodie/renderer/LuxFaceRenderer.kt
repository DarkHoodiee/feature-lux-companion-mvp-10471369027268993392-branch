package com.lux.hoodie.renderer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import com.lux.hoodie.domain.LuxFaceState

val LuxCerulean = Color(0xFF00A8E8)
val VisorBlack = Color(0xFF050505)

@Composable
fun LuxFaceRenderer(
    state: LuxFaceState,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(VisorBlack)
    ) {
        if (!state.isAodMode) {
            drawVisorRaster()
        }

        if (state.visorOpacity > 0f) {
            drawEyes(state)
        }

        if (!state.isAodMode) {
            drawVisorGlow(state.globalGlowIntensity)
        }
    }
}

private fun DrawScope.drawEyes(state: LuxFaceState) {
    val eyeWidth = size.width * 0.25f
    val eyeHeight = size.width * 0.25f

    val horizontalSpacing = size.width * 0.18f
    val centerY = size.height * 0.5f

    val color = if (state.isAodMode) LuxCerulean.copy(alpha = 0.6f) else LuxCerulean

    // Left Eye
    withTransform({
        translate(
            left = size.width / 2f - horizontalSpacing - eyeWidth / 2f + state.leftEyeOffset.first + state.aodFloatingOffset.first,
            top = centerY - eyeHeight / 2f + state.leftEyeOffset.second + state.aodFloatingOffset.second
        )
    }) {
        val path = EyePathBuilder.buildPath(state.leftEye, eyeWidth, eyeHeight, EyeSide.Left)
        drawPath(path, color, alpha = state.visorOpacity)
    }

    // Right Eye
    withTransform({
        translate(
            left = size.width / 2f + horizontalSpacing - eyeWidth / 2f + state.rightEyeOffset.first + state.aodFloatingOffset.first,
            top = centerY - eyeHeight / 2f + state.rightEyeOffset.second + state.aodFloatingOffset.second
        )
    }) {
        val path = EyePathBuilder.buildPath(state.rightEye, eyeWidth, eyeHeight, EyeSide.Right)
        drawPath(path, color, alpha = state.visorOpacity)
    }
}

private fun DrawScope.drawVisorRaster() {
    val lineCount = 40
    val lineHeight = size.height / lineCount

    for (i in 0 until lineCount) {
        if (i % 2 == 0) {
            drawRect(
                color = Color.Black.copy(alpha = 0.15f),
                topLeft = Offset(0f, i * lineHeight),
                size = androidx.compose.ui.geometry.Size(size.width, lineHeight)
            )
        }
    }
}

private fun DrawScope.drawVisorGlow(intensity: Float) {
    // Glow effects disabled for AOD
}
