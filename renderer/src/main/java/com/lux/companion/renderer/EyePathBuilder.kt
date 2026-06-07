package com.lux.companion.renderer

import androidx.compose.ui.graphics.Path
import com.lux.companion.domain.EyeTopology

object EyePathBuilder {

    fun buildPath(topology: EyeTopology): Path {
        val path = Path()

        // Start at upper left
        path.moveTo(topology.upperLeft.x, topology.upperLeft.y)

        // Upper curve
        path.cubicTo(
            topology.upperLeftMid.x, topology.upperLeftMid.y,
            topology.upperRightMid.x, topology.upperRightMid.y,
            topology.upperRight.x, topology.upperRight.y
        )

        // Lower curve
        path.cubicTo(
            topology.lowerRightMid.x, topology.lowerRightMid.y,
            topology.lowerLeftMid.x, topology.lowerLeftMid.y,
            topology.lowerLeft.x, topology.lowerLeft.y
        )

        path.close()
        return path
    }
}
