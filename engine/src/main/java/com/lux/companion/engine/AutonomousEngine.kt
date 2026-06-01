package com.lux.companion.engine

import com.lux.companion.domain.LuxFaceState
import com.lux.companion.domain.LuxMood
import com.lux.companion.domain.LuxExpression
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.sin
import kotlin.random.Random

class AutonomousEngine(
    private val scope: CoroutineScope
) {
    private val _state = MutableStateFlow(LuxFaceState())
    val state: StateFlow<LuxFaceState> = _state.asStateFlow()

    init {
        startBehaviors()
    }

    private fun startBehaviors() {
        scope.launch { startIdleLoop() }
        scope.launch { startBlinkLoop() }
        scope.launch { startBreathingLoop() }
        scope.launch { startFloatingLoop() }
    }

    private suspend fun startIdleLoop() {
        while (true) {
            delay(BehaviorSystem.getNextBehaviorDelay())
            val nextExpression = BehaviorSystem.getRandomExpression(_state.value.mood)

            // Randomly decide to scan instead
            if (Random.nextFloat() < 0.1f) {
                runScanSequence()
            } else if (Random.nextFloat() < 0.05f) {
                runRefreshSequence()
            } else {
                updateExpression(nextExpression)
                updateLookAt(Random.nextFloat() * 2 - 1, Random.nextFloat() * 2 - 1)
            }
        }
    }

    private suspend fun startBlinkLoop() {
        while (true) {
            delay(Random.nextLong(2000, 6000))
            _state.update { it.copy(eyeState = it.eyeState.copy(isBlinking = true)) }

            // Blink animation duration
            for (i in 0..10) {
                val progress = if (i <= 5) i / 5f else 1f - (i - 5) / 5f
                _state.update { it.copy(eyeState = it.eyeState.copy(blinkProgress = progress)) }
                delay(16)
            }

            _state.update { it.copy(eyeState = it.eyeState.copy(isBlinking = false, blinkProgress = 0f)) }
        }
    }

    private suspend fun startBreathingLoop() {
        var time = 0f
        while (true) {
            time += 0.05f
            // Simplified breathing vertical motion
            val breathingOffset = sin(time) * 2f
            _state.update { it.copy(verticalOffset = breathingOffset) }
            delay(32)
        }
    }

    private suspend fun startFloatingLoop() {
        var time = 0f
        while (true) {
            time += 0.02f
            val rotation = sin(time * 0.7f) * 2f
            _state.update { it.copy(rotationZ = rotation) }
            delay(32)
        }
    }

    private suspend fun runScanSequence() {
        _state.update { it.copy(isScanning = true, eyeState = it.eyeState.copy(expression = LuxExpression.SCANNING)) }
        for (i in 0..100) {
            _state.update { it.copy(scanProgress = i / 100f) }
            delay(20)
        }
        _state.update { it.copy(isScanning = false, eyeState = it.eyeState.copy(expression = LuxExpression.NEUTRAL)) }
    }

    private suspend fun runRefreshSequence() {
        _state.update { it.copy(isRefreshing = true, refreshProgress = 0.8f) }
        delay(200)
        _state.update { it.copy(refreshProgress = 0.1f) }
        delay(100)
        for (i in 10..100 step 5) {
            _state.update { it.copy(refreshProgress = i / 100f) }
            delay(16)
        }
        _state.update { it.copy(isRefreshing = false) }
    }

    fun updateExpression(expression: LuxExpression) {
        _state.update { it.copy(eyeState = it.eyeState.copy(expression = expression)) }
    }

    fun updateLookAt(x: Float, y: Float) {
        _state.update { it.copy(eyeState = it.eyeState.copy(lookAtX = x, lookAtY = y)) }
    }
}
