package com.lux.companion.domain

/**
 * Defines the parametric contour of a single eye.
 * Instead of simple rectangles, we use curves and compressions to mimic EVE's organic eyes.
 */
data class EyeGeometry(
    val width: Float = 120f,
    val height: Float = 150f,
    val tilt: Float = 0f,            // Individual eye tilt
    val upperCurve: Float = 1f,      // 0 = flat, 1 = normal arc, > 1 = bulging
    val lowerCurve: Float = 1f,
    val innerCompression: Float = 0f, // 0 = normal, 1 = very sharp/flat corner
    val outerCompression: Float = 0f,
    val shear: Float = 0f            // Horizontal skew
) {
    fun lerp(target: EyeGeometry, fraction: Float): EyeGeometry {
        return EyeGeometry(
            width = lerp(width, target.width, fraction),
            height = lerp(height, target.height, fraction),
            tilt = lerp(tilt, target.tilt, fraction),
            upperCurve = lerp(upperCurve, target.upperCurve, fraction),
            lowerCurve = lerp(lowerCurve, target.lowerCurve, fraction),
            innerCompression = lerp(innerCompression, target.innerCompression, fraction),
            outerCompression = lerp(outerCompression, target.outerCompression, fraction),
            shear = lerp(shear, target.shear, fraction)
        )
    }

    private fun lerp(start: Float, stop: Float, fraction: Float): Float {
        return start + (stop - start) * fraction
    }
}
