package com.lux.companion.renderer

import androidx.compose.ui.graphics.Path
import com.lux.companion.domain.EyeGeometry

/**
 * Constructs a Compose Path from EyeGeometry.
 * Uses a consistent topology of 4 cubic Bezier segments to ensure smooth morphing.
 */
object EyePathBuilder {

    fun buildEyePath(geometry: EyeGeometry): Path {
        val path = Path()
        val w = geometry.width
        val h = geometry.height

        // Define key points for the 4 corners of the "eye box"
        // Applying shear and tilt transformations via geometry parameters

        val halfW = w / 2f
        val halfH = h / 2f

        // Control point offsets for curvature (approx 0.552 for a circle)
        val kUpper = 0.552f * geometry.upperCurve
        val kLower = 0.552f * geometry.lowerCurve

        // Points: Top-Center, Right-Center, Bottom-Center, Left-Center
        // We compress the horizontal control points to create sharper "corners" if needed

        val topY = -halfH
        val bottomY = halfH
        val leftX = -halfW
        val rightX = halfW

        // Apply Shear (skew)
        val topXOffset = geometry.shear * halfH
        val bottomXOffset = -geometry.shear * halfH

        path.moveTo(topXOffset, topY)

        // To Right-Center
        path.cubicTo(
            x1 = topXOffset + halfW * kUpper * (1f - geometry.outerCompression), y1 = topY,
            x2 = rightX, y2 = -halfH * kUpper,
            x3 = rightX, y3 = 0f
        )

        // To Bottom-Center
        path.cubicTo(
            x1 = rightX, y1 = halfH * kLower,
            x2 = bottomXOffset + halfW * kLower * (1f - geometry.outerCompression), y2 = bottomY,
            x3 = bottomXOffset, y3 = bottomY
        )

        // To Left-Center
        path.cubicTo(
            x1 = bottomXOffset - halfW * kLower * (1f - geometry.innerCompression), y1 = bottomY,
            x2 = leftX, y2 = halfH * kLower,
            x3 = leftX, y3 = 0f
        )

        // Back to Top-Center
        path.cubicTo(
            x1 = leftX, y1 = -halfH * kUpper,
            x2 = topXOffset - halfW * kUpper * (1f - geometry.innerCompression), y2 = topY,
            x3 = topXOffset, y3 = topY
        )

        path.close()
        return path
    }
}
