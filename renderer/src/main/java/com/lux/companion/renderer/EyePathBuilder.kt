package com.lux.companion.renderer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import com.lux.companion.domain.EyeTopology

enum class EyeSide {
    Left,
    Right
}

/**
 * Constructs a Compose Path from EyeTopology.
 * Uses a consistent topology of 12 points and cubic Bezier segments to ensure smooth morphing.
 */
object EyePathBuilder {

    fun buildEyePath(
        topology: EyeTopology,
        width: Float = 100f,
        height: Float = 100f,
        side: EyeSide = EyeSide.Left
    ): Path {

        val points = CanonicalEyePoints.map { point ->
            deformCanonicalPoint(
                point = point,
                topology = topology,
                side = side
            )
        }

        val tension = 0.16f + topology.softness.coerceIn(0f, 1f) * 0.025f

        return Path().apply {
            val count = points.size
            val first = points.first()

            moveTo(
                x = first.x * width,
                y = first.y * height
            )

            for (index in points.indices) {
                val previous = points[(index - 1 + count) % count]
                val current = points[index]
                val next = points[(index + 1) % count]
                val afterNext = points[(index + 2) % count]

                cubicTo(
                    x1 = (current.x + (next.x - previous.x) * tension) * width,
                    y1 = (current.y + (next.y - previous.y) * tension) * height,
                    x2 = (next.x - (afterNext.x - current.x) * tension) * width,
                    y2 = (next.y - (afterNext.y - current.y) * tension) * height,
                    x3 = next.x * width,
                    y3 = next.y * height
                )
            }
            close()
        }
    }

    private fun deformCanonicalPoint(
        point: Offset,
        topology: EyeTopology,
        side: EyeSide
    ): Offset {
        val center = 0.5f

        val upperClose = (topology.upperLidInset + topology.upperCurve * 0.16f)
            .coerceIn(-0.18f, 0.48f)

        val lowerClose = (topology.lowerLidInset + topology.lowerCurve * 0.16f)
            .coerceIn(-0.18f, 0.48f)

        val upperScale = (1f - upperClose).coerceIn(0.42f, 1.18f)
        val lowerScale = (1f - lowerClose).coerceIn(0.42f, 1.18f)

        val sideWeight = kotlin.math.abs(point.x - center) * 2f
        val tipWeight = sideWeight.coerceIn(0f, 1f)
        val tipWeightSquared = tipWeight * tipWeight

        val tipSoftClose = (topology.cornerPinch * 0.10f + topology.taper * 0.08f)
            .coerceIn(-0.10f, 0.18f) * tipWeightSquared

        var y = if (point.y < center) {
            center - (center - point.y) * upperScale
        } else {
            center + (point.y - center) * lowerScale
        }

        y = center + (y - center) * (1f - tipSoftClose)

        var x = point.x

        val innerSide = when (side) {
            EyeSide.Left -> point.x > center
            EyeSide.Right -> point.x < center
        }

        val innerCompression = topology.innerCompression.coerceIn(0f, 1f)
        val outerExpansion = topology.outerExpansion.coerceIn(0f, 1f)

        if (innerSide) {
            x += (center - x) * innerCompression * 0.42f * tipWeightSquared
        } else {
            x += (x - center) * outerExpansion * 0.35f * tipWeightSquared
        }

        return Offset(
            x = x.coerceIn(0.006f, 0.994f),
            y = y.coerceIn(0.055f, 0.945f)
        )
    }

    // Canonical eye silhouette (12 points)
    private val CanonicalEyePoints = listOf(
        Offset(0.964f, 0.500f), // 0 deg (Far Right)
        Offset(0.902f, 0.605f), // 30 deg
        Offset(0.732f, 0.682f), // 60 deg
        Offset(0.500f, 0.710f), // 90 deg (Bottom Apex)
        Offset(0.268f, 0.682f), // 120 deg
        Offset(0.098f, 0.605f), // 150 deg
        Offset(0.036f, 0.500f), // 180 deg (Far Left)
        Offset(0.098f, 0.395f), // 210 deg
        Offset(0.268f, 0.318f), // 240 deg
        Offset(0.500f, 0.290f), // 270 deg (Top Apex)
        Offset(0.732f, 0.318f), // 300 deg
        Offset(0.902f, 0.395f)  // 330 deg
    )
}
