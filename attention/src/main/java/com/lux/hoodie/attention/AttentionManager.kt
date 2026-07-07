package com.lux.hoodie.attention

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class FocusPoint(val x: Float, val y: Float, val intensity: Float)

/**
 * Manages where the character is currently "looking" or attending to.
 */
class AttentionManager {
    private val _focusPoint = MutableStateFlow(FocusPoint(0.5f, 0.5f, 1f))
    val focusPoint: StateFlow<FocusPoint> = _focusPoint

    fun setFocus(x: Float, y: Float, intensity: Float = 1f) {
        _focusPoint.value = FocusPoint(x, y, intensity)
    }
}
