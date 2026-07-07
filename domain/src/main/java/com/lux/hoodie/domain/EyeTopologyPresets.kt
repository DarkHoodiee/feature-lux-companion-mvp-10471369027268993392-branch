package com.lux.hoodie.domain

/**
 * Maps semantic expressions to their target topology parameters.
 */
object EyeTopologyPresets {
    val Neutral = EyeTopology()

    val Curious = EyeTopology(
        upperCurve = 0.4f,
        innerCompression = 0.2f,
        softness = 0.8f
    )

    val Focused = EyeTopology(
        heightScale = 0.7f,
        upperLidInset = 0.3f,
        lowerLidInset = 0.1f,
        cornerPinch = 0.4f
    )

    val Sleepy = EyeTopology(
        heightScale = 0.6f,
        upperLidInset = 0.5f,
        softness = 1.2f
    )
}

fun LuxExpression.toTopology(): EyeTopology = when (this) {
    LuxExpression.NEUTRAL -> EyeTopologyPresets.Neutral
    LuxExpression.CURIOUS -> EyeTopologyPresets.Curious
    LuxExpression.FOCUSED -> EyeTopologyPresets.Focused
    LuxExpression.SLEEPY -> EyeTopologyPresets.Sleepy
    else -> EyeTopologyPresets.Neutral
}
