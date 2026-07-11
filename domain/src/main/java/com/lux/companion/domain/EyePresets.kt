package com.lux.companion.domain

object EyePresets {
    /**
     * Canonical Neutral Eye v3.1
     * - 11-parameter dictionary aligned.
     * - Asymmetrical: fuller outer mass, tapered inner corner.
     */
    val NEUTRAL = EyeTopology(
        widthScale = 1.0f,
        heightScale = 1.0f,
        upperCurve = 1.0f,
        lowerCurve = 0.95f,
        innerCompression = 0.1f,
        outerExpansion = 0.15f,
        cornerPinch = 0.05f,
        taper = 0.4f,
        softness = 1.0f,
        upperLidInset = 0.0f,
        lowerLidInset = 0.0f,
        tilt = -18f
    )

    val CURIOUS = EyeTopology(
        widthScale = 1.1f,
        heightScale = 1.2f,
        upperCurve = 1.2f,
        lowerCurve = 0.9f,
        innerCompression = 0.0f,
        outerExpansion = 0.2f,
        cornerPinch = 0.0f,
        taper = 0.2f,
        softness = 1.1f,
        upperLidInset = 0.0f,
        lowerLidInset = 0.0f,
        tilt = -12f
    )

    val FOCUSED = EyeTopology(
        widthScale = 1.2f,
        heightScale = 0.6f,
        upperCurve = 0.4f,
        lowerCurve = 0.5f,
        innerCompression = 0.2f,
        outerExpansion = 0.1f,
        cornerPinch = 0.3f,
        taper = 0.5f,
        softness = 0.8f,
        upperLidInset = 0.2f,
        lowerLidInset = 0.1f,
        tilt = -15f
    )

    val HAPPY = EyeTopology(
        widthScale = 1.0f,
        heightScale = 0.8f,
        upperCurve = 1.3f,
        lowerCurve = 0.2f,
        innerCompression = 0.1f,
        outerExpansion = 0.2f,
        cornerPinch = 0.1f,
        taper = 0.3f,
        softness = 1.2f,
        upperLidInset = 0.0f,
        lowerLidInset = 0.4f,
        tilt = -15f
    )

    val SLEEPY = EyeTopology(
        widthScale = 0.9f,
        heightScale = 0.4f,
        upperCurve = 0.2f,
        lowerCurve = 0.7f,
        innerCompression = 0.3f,
        outerExpansion = 0.1f,
        cornerPinch = 0.4f,
        taper = 0.3f,
        softness = 0.9f,
        upperLidInset = 0.6f,
        lowerLidInset = 0.0f,
        tilt = -10f
    )
}

fun LuxExpression.toTopology(): EyeTopology = when (this) {
    LuxExpression.NEUTRAL -> EyePresets.NEUTRAL
    LuxExpression.CURIOUS -> EyePresets.CURIOUS
    LuxExpression.FOCUSED -> EyePresets.FOCUSED
    LuxExpression.HAPPY -> EyePresets.HAPPY
    LuxExpression.SLEEPY -> EyePresets.SLEEPY
    else -> EyePresets.NEUTRAL
}
