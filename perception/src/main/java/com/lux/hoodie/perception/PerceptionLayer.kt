package com.lux.hoodie.perception

import com.lux.hoodie.domain.GestureEvent
import com.lux.hoodie.domain.GestureType
import com.lux.hoodie.events.EventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Translates low-level sensor/touch data into high-level events.
 */
class PerceptionLayer(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {
    fun onTouch(x: Float, y: Float) {
        scope.launch {
            EventBus.publish(GestureEvent(GestureType.SINGLE_TAP))
        }
    }

    fun onDeviceShake() {
        scope.launch {
            EventBus.publish(GestureEvent(GestureType.DEVICE_PICKUP))
        }
    }
}
