package com.lux.hoodie.domain

/**
 * Represents the full visual state of LUX's face.
 */
data class LuxFaceState(
    val leftEye: EyeTopology = EyeTopology.Neutral,
    val rightEye: EyeTopology = EyeTopology.Neutral,
    val leftEyeOffset: Pair<Float, Float> = Pair(0f, 0f),
    val rightEyeOffset: Pair<Float, Float> = Pair(0f, 0f),
    val visorOpacity: Float = 1f,
    val globalGlowIntensity: Float = 1f,
    val isAodMode: Boolean = false,
    val aodFloatingOffset: Pair<Float, Float> = Pair(0f, 0f)
)
