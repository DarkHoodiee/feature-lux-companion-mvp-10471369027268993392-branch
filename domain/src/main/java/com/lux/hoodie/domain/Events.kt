package com.lux.hoodie.domain

/**
 * Base interface for all system events.
 */
interface LuxEvent {
    val timestamp: Long
}

data class GestureEvent(
    val type: GestureType,
    override val timestamp: Long = System.currentTimeMillis()
) : LuxEvent

enum class GestureType {
    SINGLE_TAP,
    DOUBLE_TAP,
    LONG_PRESS,
    SWIPE_UP,
    SWIPE_DOWN,
    DEVICE_PICKUP,
    SCREEN_WAKE
}
