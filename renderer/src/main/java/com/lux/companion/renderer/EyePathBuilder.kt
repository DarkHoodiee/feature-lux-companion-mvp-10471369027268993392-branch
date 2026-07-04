package com.lux.companion.renderer

import androidx.compose.ui.graphics.Path
import com.lux.companion.domain.EyeGeometry

/**
 * Constructs a Compose Path from EyeGeometry.
 * Uses a consistent topology of 4 cubic Bezier segments to ensure smooth morphing.
 * Refined for asymmetrical Canonical Neutral Eye (v3.1).
 */
object EyePathBuilder {

    private var cachedGeometry: EyeGeometry? = null
    private var cachedPath: Path? = null

    /**
     * Builds or returns a cached Path for the given geometry.
     * Note: Since Path is mutable, this assumes the consumer does not modify the result.
     * In a multi-threaded environment, this would need synchronization, but here it's
     * called from the single-threaded Compose DrawScope.
     */
    fun buildEyePath(geometry: EyeGeometry): Path {
        if (geometry == cachedGeometry) {
            cachedPath?.let { return it }
        }

        val path = Path()
        val w = geometry.width
        val h = geometry.height

        val halfW = w / 2f
        val halfH = h / 2f

        // k-constant for circle approximation (0.5522)
        val kBase = 0.5522f * geometry.softness

        val kUpper = kBase * geometry.upperCurve
        val kLower = kBase * geometry.lowerCurve

        // Vertices
        val topY = -halfH
        val bottomY = halfH
        val leftX = -halfW
        val rightX = halfW

        val innerKMult = 1f - (geometry.innerTaper * 0.6f)
        val outerKMult = 1f + (geometry.outerExpansion * 0.4f)

        // Shear (skew)
        val topXOffset = geometry.shear * halfH
        val bottomXOffset = -geometry.shear * halfH

        path.moveTo(topXOffset, topY)

        // 1. Top-Right (Outer Upper)
        path.cubicTo(
            x1 = topXOffset + halfW * kUpper * outerKMult, y1 = topY,
            x2 = rightX, y2 = -halfH * kUpper * outerKMult,
            x3 = rightX, y3 = 0f
        )

        // 2. Bottom-Right (Outer Lower)
        path.cubicTo(
            x1 = rightX, y1 = halfH * kLower * outerKMult,
            x2 = bottomXOffset + halfW * kLower * outerKMult, y2 = bottomY,
            x3 = bottomXOffset, y3 = bottomY
        )

        // 3. Bottom-Left (Inner Lower)
        path.cubicTo(
            x1 = bottomXOffset - halfW * kLower * innerKMult, y1 = bottomY,
            x2 = leftX, y2 = halfH * kLower * innerKMult,
            x3 = leftX, y3 = 0f
        )

        // 4. Top-Left (Inner Upper)
        path.cubicTo(
            x1 = leftX, y1 = -halfH * kUpper * innerKMult,
            x2 = topXOffset - halfW * kUpper * innerKMult, y2 = topY,
            x3 = topXOffset, y3 = topY
        )

        path.close()

        cachedGeometry = geometry
        cachedPath = path

        return path
    }
}
