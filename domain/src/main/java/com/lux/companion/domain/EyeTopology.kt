package com.lux.companion.domain

/**
 * Defines the 11-parameter parametric topology of a single LUX eye.
 * This is the "Dictionary" that drives the Illusion of Life.
 */
data class EyeTopology(
    val widthScale: Float = 1.0f,
    val heightScale: Float = 1.0f,
    val upperCurve: Float = 1.0f,      // 0 = flat, 1 = normal arc
    val lowerCurve: Float = 1.0f,      // 0 = flat, 1 = normal arc
    val innerCompression: Float = 0f,  // 0 = normal, 1 = squashed inner edge
    val outerExpansion: Float = 0f,    // 0 = normal, 1 = fuller outer mass
    val cornerPinch: Float = 0f,       // 0 = round, 1 = sharp corners
    val taper: Float = 0f,             // Asymmetrical horizontal taper
    val softness: Float = 1.0f,        // Global corner roundness multiplier
    val upperLidInset: Float = 0f,     // 0 = open, 1 = closed from top
    val lowerLidInset: Float = 0f,     // 0 = open, 1 = closed from bottom
    val tilt: Float = 0f               // Rotation in degrees
) {
    fun lerp(target: EyeTopology, fraction: Float): EyeTopology {
        return EyeTopology(
            widthScale = lerp(widthScale, target.widthScale, fraction),
            heightScale = lerp(heightScale, target.heightScale, fraction),
            upperCurve = lerp(upperCurve, target.upperCurve, fraction),
            lowerCurve = lerp(lowerCurve, target.lowerCurve, fraction),
            innerCompression = lerp(innerCompression, target.innerCompression, fraction),
            outerExpansion = lerp(outerExpansion, target.outerExpansion, fraction),
            cornerPinch = lerp(cornerPinch, target.cornerPinch, fraction),
            taper = lerp(taper, target.taper, fraction),
            softness = lerp(softness, target.softness, fraction),
            upperLidInset = lerp(upperLidInset, target.upperLidInset, fraction),
            lowerLidInset = lerp(lowerLidInset, target.lowerLidInset, fraction),
            tilt = lerp(tilt, target.tilt, fraction)
        )
    }

    private fun lerp(start: Float, stop: Float, fraction: Float): Float {
        return start + (stop - start) * fraction
    }
}
