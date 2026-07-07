package com.lux.hoodie.behavior

import com.lux.hoodie.domain.BlinkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * Manages natural blinking independently of other behaviors.
 */
class BlinkSystem(private val scope: CoroutineScope) {
    private val _blinkState = MutableStateFlow(BlinkState())
    val blinkState: StateFlow<BlinkState> = _blinkState

    fun start() {
        scope.launch {
            while (true) {
                // Wait for next blink
                delay(2000L + Random.nextLong(4000))

                // Execute blink (fast close, slightly slower open)
                _blinkState.value = BlinkState(1f, 1f)
                delay(80)
                _blinkState.value = BlinkState(0.5f, 0.5f)
                delay(40)
                _blinkState.value = BlinkState(0f, 0f)
            }
        }
    }
}
