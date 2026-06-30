package com.lux.companion.domain

/**
 * Named [EyeTopology] presets for each expression.
 */
object EyeTopologyPresets {

    val Neutral = EyeTopology(
        widthScale           = 1.00f,
        heightScale          = 1.00f,
        upperCurve           = 0.01f,
        lowerCurve           = 0.00f,
        innerCompression     = 0.56f,
        outerExpansion       = 0.46f,
        cornerPinch          = 0.02f,
        taper                = 0.12f,
        softness             = 1.00f,
        upperLidInset        = 0.00f,
        lowerLidInset        = 0.00f
    )

    val Curious = EyeTopology(
        widthScale           = 1.02f,
        heightScale          = 1.08f,
        upperCurve           = -0.02f,
        lowerCurve           = 0.00f,
        innerCompression     = 0.34f,
        outerExpansion       = 0.56f,
        cornerPinch          = -0.02f,
        taper                = 0.08f,
        softness             = 1.00f,
        upperLidInset        = 0.00f,
        lowerLidInset        = 0.00f
    )

    val Focused = EyeTopology(
        widthScale           = 1.05f,
        heightScale          = 0.62f,
        upperCurve           = 0.04f,
        lowerCurve           = 0.02f,
        innerCompression     = 0.68f,
        outerExpansion       = 0.10f,
        cornerPinch          = 0.10f,
        taper                = 0.22f,
        softness             = 1.00f,
        upperLidInset        = 0.03f,
        lowerLidInset        = 0.03f
    )

    val Sleepy = EyeTopology(
        widthScale           = 1.02f,
        heightScale          = 0.50f,
        upperCurve           = 0.02f,
        lowerCurve           = 0.00f,
        innerCompression     = 0.50f,
        outerExpansion       = 0.22f,
        cornerPinch          = 0.04f,
        taper                = 0.14f,
        softness             = 1.00f,
        upperLidInset        = 0.14f,
        lowerLidInset        = 0.00f
    )

    val Happy = Curious.copy(
        upperCurve = 0.2f,
        lowerLidInset = 0.2f
    )

    fun forExpression(expression: LuxExpression): EyeTopology = when (expression) {
        LuxExpression.NEUTRAL -> Neutral
        LuxExpression.CURIOUS -> Curious
        LuxExpression.FOCUSED -> Focused
        LuxExpression.HAPPY   -> Happy
        LuxExpression.SLEEPY  -> Sleepy
        else -> Neutral
    }
}
