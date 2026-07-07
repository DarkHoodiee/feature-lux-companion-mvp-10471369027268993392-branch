package com.lux.hoodie.renderer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import com.lux.hoodie.domain.EyeTopology
import kotlin.math.abs

enum class EyeSide {
    Left,
    Right
}

object EyePathBuilder {

    private val CanonicalEyePoints = listOf(
        Offset(0.825f, 0.500f), // 0 deg (Far Right)
        Offset(0.781f, 0.613f), // 30 deg
        Offset(0.662f, 0.691f), // 60 deg
        Offset(0.500f, 0.720f), // 90 deg (Bottom Apex)
        Offset(0.338f, 0.691f), // 120 deg
        Offset(0.219f, 0.613f), // 150 deg
        Offset(0.175f, 0.500f), // 180 deg (Far Left)
        Offset(0.219f, 0.387f), // 210 deg
        Offset(0.338f, 0.309f), // 240 deg
        Offset(0.500f, 0.280f), // 270 deg (Top Apex)
        Offset(0.662f, 0.309f), // 300 deg
        Offset(0.781f, 0.387f)  // 330 deg
    )

    fun buildPath(
        topology: EyeTopology,
        width: Float,
        height: Float,
        side: EyeSide
    ): Path {
        val points = CanonicalEyePoints.map { point ->
            deformPoint(point, topology, side)
        }

        val tension = 0.16f + topology.softness.coerceIn(0f, 1f) * 0.025f

        return Path().apply {
            val count = points.size
            val first = points.first()

            moveTo(first.x * width, first.y * height)

            for (index in points.indices) {
                val p0 = points[(index - 1 + count) % count]
                val p1 = points[index]
                val p2 = points[(index + 1) % count]
                val p3 = points[(index + 2) % count]

                cubicTo(
                    x1 = (p1.x + (p2.x - p0.x) * tension) * width,
                    y1 = (p1.y + (p2.y - p0.y) * tension) * height,
                    x2 = (p2.x - (p3.x - p1.x) * tension) * width,
                    y2 = (p2.y - (p3.y - p1.y) * tension) * height,
                    x3 = p2.x * width,
                    y3 = p2.y * height
                )
            }
            close()
        }
    }

    private fun deformPoint(
        point: Offset,
        topology: EyeTopology,
        side: EyeSide
    ): Offset {
        val center = 0.5f

        val upperClose = (topology.upperLidInset + topology.upperCurve * 0.16f).coerceIn(-0.18f, 0.48f)
        val lowerClose = (topology.lowerLidInset + topology.lowerCurve * 0.16f).coerceIn(-0.18f, 0.48f)

        val upperScale = (1f - upperClose).coerceIn(0.42f, 1.18f)
        val lowerScale = (1f - lowerClose).coerceIn(0.42f, 1.18f)

        val sideWeight = abs(point.x - center) * 2f
        val tipWeight = sideWeight.coerceIn(0f, 1f)
        val tipWeightSquared = tipWeight * tipWeight

        val tipSoftClose = (topology.cornerPinch * 0.10f + topology.taper * 0.08f).coerceIn(-0.10f, 0.18f) * tipWeightSquared

        var y = if (point.y < center) {
            center - (center - point.y) * upperScale
        } else {
            center + (point.y - center) * lowerScale
        }

        y = center + (y - center) * (1f - tipSoftClose)

        var x = point.x
        val isInnerSide = when (side) {
            EyeSide.Left -> point.x > center
            EyeSide.Right -> point.x < center
        }

        if (isInnerSide) {
            x += (center - x) * topology.innerCompression.coerceIn(0f, 1f) * 0.42f * tipWeightSquared
        } else {
            x += (x - center) * topology.outerExpansion.coerceIn(0f, 1f) * 0.35f * tipWeightSquared
        }

        // Apply width and height scaling
        x = center + (x - center) * topology.widthScale
        y = center + (y - center) * topology.heightScale

        return Offset(
            x = x.coerceIn(0.006f, 0.994f),
            y = y.coerceIn(0.055f, 0.945f)
        )
    }
}
