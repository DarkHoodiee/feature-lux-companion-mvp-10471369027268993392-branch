package com.lux.companion.domain

object EyePresets {
    /**
     * Canonical Neutral Eye v3.1
     * - Asymmetrical: fuller outer mass, tapered inner corner.
     * - Organic: avoid capsule/rect appearance.
     */
    val NEUTRAL = EyeGeometry(
        width = 130f,
        height = 165f,
        upperCurve = 1.0f,
        lowerCurve = 0.95f,
        innerTaper = 0.4f,
        outerExpansion = 0.15f,
        softness = 1.0f,
        tilt = -18f
    )

    val CURIOUS = EyeGeometry(
        width = 140f,
        height = 185f,
        upperCurve = 1.2f,
        lowerCurve = 0.9f,
        innerTaper = 0.2f,
        outerExpansion = 0.2f,
        softness = 1.1f,
        tilt = -12f
    )

    val FOCUSED = EyeGeometry(
        width = 155f,
        height = 80f,
        upperCurve = 0.4f,
        lowerCurve = 0.5f,
        innerTaper = 0.5f,
        outerExpansion = 0.1f,
        softness = 0.8f,
        tilt = -15f
    )

    val HAPPY = EyeGeometry(
        width = 135f,
        height = 120f,
        upperCurve = 1.3f,
        lowerCurve = 0.2f,
        innerTaper = 0.3f,
        outerExpansion = 0.2f,
        softness = 1.2f,
        tilt = -15f
    )

    val SLEEPY = EyeGeometry(
        width = 120f,
        height = 60f,
        upperCurve = 0.2f,
        lowerCurve = 0.7f,
        innerTaper = 0.3f,
        outerExpansion = 0.1f,
        softness = 0.9f,
        tilt = -10f
    )
}

fun LuxExpression.toGeometry(): EyeGeometry = when (this) {
    LuxExpression.NEUTRAL -> EyePresets.NEUTRAL
    LuxExpression.CURIOUS -> EyePresets.CURIOUS
    LuxExpression.FOCUSED -> EyePresets.FOCUSED
    LuxExpression.HAPPY -> EyePresets.HAPPY
    LuxExpression.SLEEPY -> EyePresets.SLEEPY
    else -> EyePresets.NEUTRAL
}
