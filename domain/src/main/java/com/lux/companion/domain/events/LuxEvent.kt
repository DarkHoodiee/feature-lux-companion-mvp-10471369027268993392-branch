package com.lux.companion.domain.events

/**
 * Unified event types for the LUX cognitive system.
 */
sealed class LuxEvent {
    data class Touch(val x: Float, val y: Float) : LuxEvent()
    data class Interaction(val type: InteractionType) : LuxEvent()
    data object IdleTimerExpired : LuxEvent()
    data object WakeUp : LuxEvent()
}

enum class InteractionType {
    TAP, DOUBLE_TAP, LONG_PRESS
}
