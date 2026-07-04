package com.lux.companion.engine.attention

import com.lux.companion.domain.events.LuxEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Decouples sensors and interaction handlers from the Engine.
 */
object EventBus {
    private val _events = MutableSharedFlow<LuxEvent>()
    val events = _events.asSharedFlow()

    suspend fun publish(event: LuxEvent) {
        _events.emit(event)
    }
}
