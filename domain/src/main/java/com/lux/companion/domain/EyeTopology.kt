package com.lux.companion.domain

/**
 * Defines the parametric topology of a single eye.
 * Based on EVE's organic, non-angular contours.
 */
data class EyeTopology(
    val widthScale: Float = 1f,
    val heightScale: Float = 1f,
    val upperCurve: Float = 0f,
    val lowerCurve: Float = 0f,
    val innerCompression: Float = 0f,
    val outerExpansion: Float = 0f,
    val cornerPinch: Float = 0f,
    val taper: Float = 0f,
    val softness: Float = 1f,
    val upperLidInset: Float = 0f,
    val lowerLidInset: Float = 0f
)
