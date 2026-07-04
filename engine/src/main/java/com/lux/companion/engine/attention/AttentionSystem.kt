package com.lux.companion.engine.attention

import androidx.compose.ui.geometry.Offset
import com.lux.companion.domain.events.LuxEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Manages Cognitive Context: Attention, Memory, and Interest.
 * Implements the "Detect -> Observe -> Release" cycle.
 */
class AttentionSystem(
    private val scope: CoroutineScope
) {
    private val _focusLevel = MutableStateFlow(0f)
    val focusLevel = _focusLevel

    private val _lastInterestPoint = MutableStateFlow<Offset?>(null)
    val lastInterestPoint = _lastInterestPoint

    // Short-term memory of recent event timestamps
    private val eventHistory = mutableListOf<Long>()
    private var lastEventTime = System.currentTimeMillis()

    init {
        scope.launch {
            while (true) {
                val now = System.currentTimeMillis()
                val timeSinceEvent = now - lastEventTime

                if (timeSinceEvent > 8000) {
                    // Release attention if nothing happened for 8s
                    _focusLevel.update { (it - 0.1f).coerceAtLeast(0f) }
                    if (_focusLevel.value == 0f) {
                        _lastInterestPoint.value = null
                    }
                }

                // Satiation: cleanup history older than 30s
                eventHistory.removeAll { now - it > 30000 }
                delay(1000)
            }
        }
    }

    fun processEvent(event: LuxEvent) {
        val now = System.currentTimeMillis()
        lastEventTime = now

        // Memory/Satiation logic: frequency reduces the "impact" of new events
        val recentEventCount = eventHistory.size
        val focusGain = (0.4f / (recentEventCount + 1)).coerceAtLeast(0.05f)

        eventHistory.add(now)

        when (event) {
            is LuxEvent.Interaction -> {
                _focusLevel.update { (it + focusGain).coerceAtMost(1f) }
            }
            is LuxEvent.Touch -> {
                _focusLevel.update { (it + focusGain * 0.8f).coerceAtMost(1f) }
                _lastInterestPoint.value = Offset(event.x, event.y)
            }
            is LuxEvent.IdleTimerExpired -> {
                // Curiosity-driven focus bump
                _focusLevel.update { (it + 0.1f).coerceAtMost(0.4f) }
            }
            else -> {}
        }
    }
}
