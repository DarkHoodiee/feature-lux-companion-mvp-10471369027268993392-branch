package com.lux.companion.domain

sealed class LuxInteraction {
    data class Tap(val x: Float, val y: Float) : LuxInteraction()
    data class DoubleTap(val x: Float, val y: Float) : LuxInteraction()
    data class LongPress(val x: Float, val y: Float) : LuxInteraction()
    data class RapidTaps(val x: Float, val y: Float) : LuxInteraction()
}
