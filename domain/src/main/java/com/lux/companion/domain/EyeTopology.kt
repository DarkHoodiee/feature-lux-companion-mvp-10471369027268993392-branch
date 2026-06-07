package com.lux.companion.domain

import androidx.compose.ui.geometry.Offset

data class EyeTopology(
    // Upper row (left to right)
    val upperLeft: Offset,
    val upperLeftMid: Offset,
    val upperCenter: Offset,
    val upperRightMid: Offset,
    val upperRight: Offset,

    // Lower row (right to left for a continuous loop)
    val lowerRight: Offset,
    val lowerRightMid: Offset,
    val lowerCenter: Offset,
    val lowerLeftMid: Offset,
    val lowerLeft: Offset
) {
    companion object {
        fun createNeutral(width: Float, height: Float): EyeTopology {
            val w = width
            val h = height
            return EyeTopology(
                upperLeft = Offset(0f, h * 0.5f),
                upperLeftMid = Offset(w * 0.2f, h * 0.1f),
                upperCenter = Offset(w * 0.5f, 0f),
                upperRightMid = Offset(w * 0.8f, h * 0.1f),
                upperRight = Offset(w, h * 0.5f),

                lowerRight = Offset(w, h * 0.5f),
                lowerRightMid = Offset(w * 0.8f, h * 0.9f),
                lowerCenter = Offset(w * 0.5f, h),
                lowerLeftMid = Offset(w * 0.2f, h * 0.9f),
                lowerLeft = Offset(0f, h * 0.5f)
            )
        }
    }
}

fun lerp(start: Offset, stop: Offset, fraction: Float): Offset {
    return Offset(
        start.x + (stop.x - start.x) * fraction,
        start.y + (stop.y - start.y) * fraction
    )
}

fun EyeTopology.lerp(target: EyeTopology, fraction: Float): EyeTopology {
    return EyeTopology(
        upperLeft = lerp(this.upperLeft, target.upperLeft, fraction),
        upperLeftMid = lerp(this.upperLeftMid, target.upperLeftMid, fraction),
        upperCenter = lerp(this.upperCenter, target.upperCenter, fraction),
        upperRightMid = lerp(this.upperRightMid, target.upperRightMid, fraction),
        upperRight = lerp(this.upperRight, target.upperRight, fraction),
        lowerRight = lerp(this.lowerRight, target.lowerRight, fraction),
        lowerRightMid = lerp(this.lowerRightMid, target.lowerRightMid, fraction),
        lowerCenter = lerp(this.lowerCenter, target.lowerCenter, fraction),
        lowerLeftMid = lerp(this.lowerLeftMid, target.lowerLeftMid, fraction),
        lowerLeft = lerp(this.lowerLeft, target.lowerLeft, fraction)
    )
}
