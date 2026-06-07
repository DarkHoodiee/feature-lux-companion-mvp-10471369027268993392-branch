package com.lux.companion.engine

import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.PI

object Easing {
    fun anticipateOvershoot(t: Float): Float {
        val s = 1.70158f * 1.525f
        return if (t < 0.5) {
            0.5f * (2 * t).let { it * it * ((s + 1) * it - s) }
        } else {
            0.5f * ((2 * t - 2).let { it * it * ((s + 1) * it + s) } + 2)
        }
    }
}
