package com.lux.companion.domain

object EyePresets {
    // A stable, slightly rounded capsule.
    val NEUTRAL = EyeGeometry(
        width = 120f,
        height = 160f,
        upperCurve = 1.0f,
        lowerCurve = 1.0f,
        innerCompression = 0.05f,
        outerCompression = 0.05f
    )

    // Curiosity is about openness and investigation. Taller, softer, less angular.
    val CURIOUS = EyeGeometry(
        width = 135f,
        height = 180f,
        upperCurve = 1.1f,
        lowerCurve = 0.9f,
        innerCompression = 0.0f,
        outerCompression = 0.0f,
        tilt = -8f
    )

    // Focus is a deformation of the capsule, flattened and squinting.
    val FOCUSED = EyeGeometry(
        width = 150f,
        height = 70f,
        upperCurve = 0.4f,
        lowerCurve = 0.4f,
        innerCompression = 0.3f,
        outerCompression = 0.3f
    )

    // Happy is the bottom edge lifting up while the top stays soft.
    val HAPPY = EyeGeometry(
        width = 130f,
        height = 110f,
        upperCurve = 1.2f,
        lowerCurve = 0.1f, // The "lift"
        innerCompression = 0.1f,
        outerCompression = 0.1f
    )

    val SLEEPY = EyeGeometry(
        width = 115f,
        height = 50f,
        upperCurve = 0.1f,
        lowerCurve = 0.6f,
        innerCompression = 0.2f,
        outerCompression = 0.2f
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
