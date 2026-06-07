package com.lux.companion.renderer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import com.lux.companion.domain.LuxExpression

object EyePathProvider {

    fun getEyePath(expression: LuxExpression, size: Size): Path {
        val path = Path()
        val width = size.width
        val height = size.height

        when (expression) {
            LuxExpression.NEUTRAL -> {
                path.addOval(Rect(Offset.Zero, size))
            }
            LuxExpression.HAPPY -> {
                // Smiling upper curve, flat bottom
                path.moveTo(0f, height * 0.5f)
                path.quadraticBezierTo(width * 0.5f, -height * 0.2f, width, height * 0.5f)
                path.lineTo(width, height * 0.8f)
                path.quadraticBezierTo(width * 0.5f, height, 0f, height * 0.8f)
                path.close()
            }
            LuxExpression.CURIOUS -> {
                // Asymmetrical, slightly raised upper edge
                path.moveTo(0f, height * 0.3f)
                path.quadraticBezierTo(width * 0.4f, -height * 0.1f, width, height * 0.2f)
                path.quadraticBezierTo(width * 1.1f, height * 0.7f, width * 0.8f, height * 0.9f)
                path.quadraticBezierTo(width * 0.4f, height * 1.1f, 0f, height * 0.8f)
                path.close()
            }
            LuxExpression.FOCUSED -> {
                // Narrow slit-like eyes
                path.addRect(Rect(0f, height * 0.4f, width, height * 0.6f))
            }
            LuxExpression.SLEEPY -> {
                // Drooping upper eyelids
                path.moveTo(0f, height * 0.7f)
                path.quadraticBezierTo(width * 0.5f, height * 0.5f, width, height * 0.7f)
                path.quadraticBezierTo(width * 0.5f, height * 1.1f, 0f, height * 0.7f)
                path.close()
            }
            LuxExpression.THINKING -> {
                // Partially narrowed eyes
                path.moveTo(0f, height * 0.3f)
                path.quadraticBezierTo(width * 0.5f, height * 0.2f, width, height * 0.4f)
                path.lineTo(width, height * 0.8f)
                path.quadraticBezierTo(width * 0.5f, height * 0.9f, 0f, height * 0.8f)
                path.close()
            }
            LuxExpression.SCANNING -> {
                // Horizontal visor-style
                path.addRoundRect(
                    androidx.compose.ui.geometry.RoundRect(
                        Rect(0f, height * 0.45f, width, height * 0.55f),
                        androidx.compose.ui.geometry.CornerRadius(height * 0.05f)
                    )
                )
            }
            LuxExpression.SURPRISED -> {
                // Tall vertical ovals
                path.addOval(Rect(width * 0.1f, -height * 0.2f, width * 0.9f, height * 1.2f))
            }
            LuxExpression.EXCITED -> {
                // Angled, vibrant shape
                path.moveTo(0f, height * 0.2f)
                path.lineTo(width, 0f)
                path.lineTo(width * 0.8f, height)
                path.lineTo(width * 0.2f, height)
                path.close()
            }
            LuxExpression.ANNOYED -> {
                // Flat top, downwards slant
                path.moveTo(0f, height * 0.4f)
                path.lineTo(width, height * 0.2f)
                path.lineTo(width, height * 0.8f)
                path.lineTo(0f, height * 0.8f)
                path.close()
            }
        }
        return path
    }
}
