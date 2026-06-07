package com.lux.companion.domain

object EyePresets {
    val NEUTRAL = EyeGeometry(
        width = 120f,
        height = 150f,
        upperCurve = 1.0f,
        lowerCurve = 1.0f,
        innerCompression = 0.1f,
        outerCompression = 0.1f
    )

    val CURIOUS = EyeGeometry(
        width = 130f,
        height = 140f,
        upperCurve = 1.2f,
        lowerCurve = 0.8f,
        innerCompression = 0.0f,
        outerCompression = 0.4f,
        tilt = -10f
    )

    val FOCUSED = EyeGeometry(
        width = 140f,
        height = 80f,
        upperCurve = 0.3f,
        lowerCurve = 0.3f,
        innerCompression = 0.5f,
        outerCompression = 0.5f
    )

    val HAPPY = EyeGeometry(
        width = 130f,
        height = 120f,
        upperCurve = 1.5f,
        lowerCurve = 0.2f,
        innerCompression = 0.2f,
        outerCompression = 0.2f
    )

    val SLEEPY = EyeGeometry(
        width = 110f,
        height = 60f,
        upperCurve = 0.1f,
        lowerCurve = 0.8f,
        innerCompression = 0.3f,
        outerCompression = 0.3f
    )

    val ANNOYED = EyeGeometry(
        width = 130f,
        height = 100f,
        upperCurve = 0.2f,
        lowerCurve = 0.5f,
        innerCompression = 0.6f,
        outerCompression = 0.1f,
        shear = 0.2f
    )
}

fun LuxExpression.toGeometry(): EyeGeometry = when (this) {
    LuxExpression.NEUTRAL -> EyePresets.NEUTRAL
    LuxExpression.CURIOUS -> EyePresets.CURIOUS
    LuxExpression.FOCUSED -> EyePresets.FOCUSED
    LuxExpression.HAPPY -> EyePresets.HAPPY
    LuxExpression.SLEEPY -> EyePresets.SLEEPY
    LuxExpression.ANNOYED -> EyePresets.ANNOYED
    else -> EyePresets.NEUTRAL
}
