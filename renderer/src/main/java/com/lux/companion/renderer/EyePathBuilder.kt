package com.lux.companion.renderer

import androidx.compose.ui.graphics.Path
import com.lux.companion.domain.EyeTopology

/**
 * Constructs a Compose Path from EyeTopology.
 * Uses a consistent topology of 4 cubic Bezier segments to ensure smooth morphing.
 * Refined for the 11-parameter LUX "Dictionary" (v3.1).
 */
object EyePathBuilder {

    private var cachedTopology: EyeTopology? = null
    private var cachedPath: Path? = null

    /**
     * Builds or returns a cached Path for the given topology.
     */
    fun buildEyePath(topology: EyeTopology): Path {
        if (topology == cachedTopology) {
            cachedPath?.let { return it }
        }

        val path = Path()

        // Base dimensions (100x100 reference unit scaled by width/height scale)
        val w = 120f * topology.widthScale
        val h = 150f * topology.heightScale

        val halfW = w / 2f
        val halfH = h / 2f

        // k-constant for circle approximation (0.5522) adjusted by softness and corner pinch
        val kBase = 0.5522f * topology.softness * (1f - topology.cornerPinch * 0.8f)

        val kUpper = kBase * topology.upperCurve
        val kLower = kBase * topology.lowerCurve

        // Vertices
        val topY = -halfH + (h * topology.upperLidInset * 0.5f)
        val bottomY = halfH - (h * topology.lowerLidInset * 0.5f)
        val leftX = -halfW
        val rightX = halfW

        // Compression and Taper
        val innerKMult = 1f - (topology.innerCompression * 0.5f)
        val outerKMult = 1f + (topology.outerExpansion * 0.4f)
        val taperOffset = topology.taper * halfW

        // Shear/Taper Vertices
        val vTopX = taperOffset
        val vBottomX = -taperOffset

        path.moveTo(vTopX, topY)

        // 1. Top-Right (Outer Upper)
        path.cubicTo(
            x1 = vTopX + halfW * kUpper * outerKMult, y1 = topY,
            x2 = rightX, y2 = topY + halfH * kUpper * outerKMult,
            x3 = rightX, y3 = 0f
        )

        // 2. Bottom-Right (Outer Lower)
        path.cubicTo(
            x1 = rightX, y1 = bottomY - halfH * kLower * outerKMult,
            x2 = vBottomX + halfW * kLower * outerKMult, y2 = bottomY,
            x3 = vBottomX, y3 = bottomY
        )

        // 3. Bottom-Left (Inner Lower)
        path.cubicTo(
            x1 = vBottomX - halfW * kLower * innerKMult, y1 = bottomY,
            x2 = leftX, y2 = bottomY - halfH * kLower * innerKMult,
            x3 = leftX, y3 = 0f
        )

        // 4. Top-Left (Inner Upper)
        path.cubicTo(
            x1 = leftX, y1 = topY + halfH * kUpper * innerKMult,
            x2 = vTopX - halfW * kUpper * innerKMult, y2 = topY,
            x3 = vTopX, y3 = topY
        )

        path.close()

        cachedTopology = topology
        cachedPath = path

        return path
    }
}
