package com.lux.companion.domain

data class EyeState(
    val expression: LuxExpression = LuxExpression.NEUTRAL,
    val lookAtX: Float = 0f, // -1f to 1f
    val lookAtY: Float = 0f, // -1f to 1f
    val scaleX: Float = 1f,
    val scaleY: Float = 1f,
    val isBlinking: Boolean = false,
    val blinkProgress: Float = 0f, // 0f to 1f
    val glowIntensity: Float = 0.5f // 0f to 1f
)
