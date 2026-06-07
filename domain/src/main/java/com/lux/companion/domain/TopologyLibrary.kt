package com.lux.companion.domain

import androidx.compose.ui.geometry.Offset

object TopologyLibrary {

    fun getTopology(expression: LuxExpression, width: Float, height: Float): EyeTopology {
        val w = width
        val h = height

        return when (expression) {
            LuxExpression.NEUTRAL -> EyeTopology.createNeutral(w, h)

            LuxExpression.CURIOUS -> EyeTopology(
                upperLeft = Offset(0f, h * 0.3f),
                upperLeftMid = Offset(w * 0.3f, h * 0.05f),
                upperCenter = Offset(w * 0.6f, h * 0.1f),
                upperRightMid = Offset(w * 0.9f, h * 0.2f),
                upperRight = Offset(w, h * 0.4f),

                lowerRight = Offset(w, h * 0.4f),
                lowerRightMid = Offset(w * 0.8f, h * 0.95f),
                lowerCenter = Offset(w * 0.5f, h * 1.05f),
                lowerLeftMid = Offset(w * 0.2f, h * 0.9f),
                lowerLeft = Offset(0f, h * 0.3f)
            )

            LuxExpression.FOCUSED -> EyeTopology(
                upperLeft = Offset(0f, h * 0.45f),
                upperLeftMid = Offset(w * 0.25f, h * 0.4f),
                upperCenter = Offset(w * 0.5f, h * 0.4f),
                upperRightMid = Offset(w * 0.75f, h * 0.4f),
                upperRight = Offset(w, h * 0.45f),

                lowerRight = Offset(w, h * 0.45f),
                lowerRightMid = Offset(w * 0.75f, h * 0.6f),
                lowerCenter = Offset(w * 0.5f, h * 0.6f),
                lowerLeftMid = Offset(w * 0.25f, h * 0.6f),
                lowerLeft = Offset(0f, h * 0.45f)
            )

            LuxExpression.ANNOYED -> EyeTopology(
                upperLeft = Offset(0f, h * 0.2f),
                upperLeftMid = Offset(w * 0.4f, h * 0.3f),
                upperCenter = Offset(w * 0.6f, h * 0.35f),
                upperRightMid = Offset(w * 0.8f, h * 0.4f),
                upperRight = Offset(w, h * 0.45f),

                lowerRight = Offset(w, h * 0.45f),
                lowerRightMid = Offset(w * 0.75f, h * 0.8f),
                lowerCenter = Offset(w * 0.5f, h * 0.85f),
                lowerLeftMid = Offset(w * 0.25f, h * 0.8f),
                lowerLeft = Offset(0f, h * 0.2f)
            )

            LuxExpression.HAPPY -> EyeTopology(
                upperLeft = Offset(0f, h * 0.5f),
                upperLeftMid = Offset(w * 0.2f, h * 0.1f),
                upperCenter = Offset(w * 0.5f, -h * 0.1f),
                upperRightMid = Offset(w * 0.8f, h * 0.1f),
                upperRight = Offset(w, h * 0.5f),

                lowerRight = Offset(w, h * 0.5f),
                lowerRightMid = Offset(w * 0.8f, h * 0.6f),
                lowerCenter = Offset(w * 0.5f, h * 0.65f),
                lowerLeftMid = Offset(w * 0.2f, h * 0.6f),
                lowerLeft = Offset(0f, h * 0.5f)
            )

            else -> EyeTopology.createNeutral(w, h)
        }
    }
}
