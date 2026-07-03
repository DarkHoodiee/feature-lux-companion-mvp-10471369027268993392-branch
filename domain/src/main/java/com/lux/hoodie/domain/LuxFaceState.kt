package com.lux.hoodie.domain

/**
 * Represents the physical orientation and position of the character's head.
 * Supports the "Illusion of Life" through subtle movements.
 */
data class HeadState(
    val pitch: Float = 0f, // Look up/down
    val yaw: Float = 0f,   // Look left/right
    val roll: Float = 0f,  // Tilt head
    val hoverX: Float = 0f,
    val hoverY: Float = 0f,
    val scale: Float = 1f
)

/**
 * Independent layer for blinking behavior.
 */
data class BlinkState(
    val leftLidProgress: Float = 0f, // 0 = open, 1 = closed
    val rightLidProgress: Float = 0f
)

/**
 * Represents the full visual state of LUX's face and physical presence.
 */
data class LuxFaceState(
    val leftEye: EyeTopology = EyeTopology.Neutral,
    val rightEye: EyeTopology = EyeTopology.Neutral,
    val head: HeadState = HeadState(),
    val blink: BlinkState = BlinkState(),
    val leftEyeOffset: Pair<Float, Float> = Pair(0f, 0f),
    val rightEyeOffset: Pair<Float, Float> = Pair(0f, 0f),
    val visorOpacity: Float = 1f,
    val globalGlowIntensity: Float = 1f,
    val isAodMode: Boolean = false,
    val aodFloatingOffset: Pair<Float, Float> = Pair(0f, 0f)
)
