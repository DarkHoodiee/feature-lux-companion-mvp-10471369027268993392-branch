package com.lux.hoodie.events

import com.lux.hoodie.domain.LuxEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Global decoupled event bus for system-wide communication.
 */
object EventBus {
    private val _events = MutableSharedFlow<LuxEvent>()
    val events: SharedFlow<LuxEvent> = _events.asSharedFlow()

    suspend fun publish(event: LuxEvent) {
        _events.emit(event)
    }
}
