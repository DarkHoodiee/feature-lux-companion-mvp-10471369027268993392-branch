package com.lux.companion.renderer

import androidx.compose.ui.graphics.Path
import com.lux.companion.domain.EyeGeometry

/**
 * EyePathBuilder.kt
 *
 * Constructs a Compose Path from EyeGeometry.
 * Uses a consistent topology of 4 cubic Bezier segments to ensure smooth morphing.
 * Specifically tuned for EVE's organic, asymmetrical, and tapered contours.
 */
object EyePathBuilder {

    /**
     * @param isLeft Whether this is the left eye (as seen by the viewer).
     * INNER side (tapered) is towards the center/nose.
     * OUTER side (fuller) is towards the edge of the visor.
     */
    fun buildEyePath(geometry: EyeGeometry, isLeft: Boolean): Path {
        val path = Path()
        val w = geometry.width
        val h = geometry.height

        val halfW = w / 2f
        val halfH = h / 2f

        // Control point offsets for curvature
        val kUpper = 0.552f * geometry.upperCurve
        val kLower = 0.552f * geometry.lowerCurve

        // Vertical positions
        val topY = -halfH
        val bottomY = halfH

        // Horizontal positions
        val leftX = -halfW
        val rightX = halfW

        // Apply Shear (skew)
        val topXOffset = geometry.shear * halfH
        val bottomXOffset = -geometry.shear * halfH

        // LEFT eye (viewer's left): INNER is Right, OUTER is Left.
        // RIGHT eye (viewer's right): INNER is Left, OUTER is Right.
        val leftComp = if (isLeft) geometry.outerCompression else geometry.innerCompression
        val rightComp = if (isLeft) geometry.innerCompression else geometry.outerCompression

        path.moveTo(topXOffset, topY)

        // Top-Right Quadrant
        path.cubicTo(
            x1 = topXOffset + halfW * kUpper * (1f - rightComp * 0.6f), y1 = topY,
            x2 = rightX, y2 = -halfH * kUpper * (1f - rightComp * 0.3f),
            x3 = rightX, y3 = 0f
        )

        // Bottom-Right Quadrant
        path.cubicTo(
            x1 = rightX, y1 = halfH * kLower * (1f - rightComp * 0.3f),
            x2 = bottomXOffset + halfW * kLower * (1f - rightComp * 0.6f), y2 = bottomY,
            x3 = bottomXOffset, y3 = bottomY
        )

        // Bottom-Left Quadrant
        path.cubicTo(
            x1 = bottomXOffset - halfW * kLower * (1f - leftComp * 0.6f), y1 = bottomY,
            x2 = leftX, y2 = halfH * kLower * (1f - leftComp * 0.3f),
            x3 = leftX, y3 = 0f
        )

        // Top-Left Quadrant
        path.cubicTo(
            x1 = leftX, y1 = -halfH * kUpper * (1f - leftComp * 0.3f),
            x2 = topXOffset - halfW * kUpper * (1f - leftComp * 0.6f), y2 = topY,
            x3 = topXOffset, y3 = topY
        )

        path.close()
        return path
    }
}
