package com.lux.companion.domain

sealed class LuxInteraction {
    object Tap : LuxInteraction()
    object DoubleTap : LuxInteraction()
    object LongPress : LuxInteraction()
    object RapidTaps : LuxInteraction()
}
