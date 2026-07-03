package com.lux.hoodie.domain

/**
 * The 11-parameter topology model for organic eye shapes.
 * All expressions are derived by deforming these parameters.
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
) {
    companion object {
        val Neutral = EyeTopology()
    }
}
