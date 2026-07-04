package com.lux.companion.domain

/**
 * Defines the parametric contour of a single eye.
 * Aligned with v3.1 Canonical Neutral Eye specification.
 */
data class EyeGeometry(
    val width: Float = 120f,
    val height: Float = 150f,
    val tilt: Float = 0f,            // Individual eye tilt
    val upperCurve: Float = 1f,      // 0 = flat, 1 = normal arc
    val lowerCurve: Float = 1f,      // 0 = flat, 1 = normal arc
    val innerTaper: Float = 0f,      // 0 = normal, 1 = sharp inward taper
    val outerExpansion: Float = 0f,  // 0 = normal, 1 = fuller outer mass
    val softness: Float = 1f,        // Corner roundness multiplier
    val shear: Float = 0f            // Horizontal skew
) {
    fun lerp(target: EyeGeometry, fraction: Float): EyeGeometry {
        return EyeGeometry(
            width = lerp(width, target.width, fraction),
            height = lerp(height, target.height, fraction),
            tilt = lerp(tilt, target.tilt, fraction),
            upperCurve = lerp(upperCurve, target.upperCurve, fraction),
            lowerCurve = lerp(lowerCurve, target.lowerCurve, fraction),
            innerTaper = lerp(innerTaper, target.innerTaper, fraction),
            outerExpansion = lerp(outerExpansion, target.outerExpansion, fraction),
            softness = lerp(softness, target.softness, fraction),
            shear = lerp(shear, target.shear, fraction)
        )
    }

    private fun lerp(start: Float, stop: Float, fraction: Float): Float {
        return start + (stop - start) * fraction
    }
}
