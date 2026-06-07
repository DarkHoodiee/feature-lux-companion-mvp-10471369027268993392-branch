package com.lux.companion.engine

import com.lux.companion.domain.*
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
            val currentMood = _state.value.mood

            // Randomly decide to scan instead
            if (Random.nextFloat() < 0.1f) {
                runScanSequence()
            } else if (Random.nextFloat() < 0.05f) {
                runRefreshSequence()
            } else {
                val nextExpression = BehaviorSystem.getRandomExpression(currentMood)
                morphToExpression(nextExpression)

                // Random look around
                val lookX = Random.nextFloat() * 2 - 1
                val lookY = Random.nextFloat() * 2 - 1
                updateLookAt(lookX, lookY)
            }
        }
    }

    private suspend fun startBlinkLoop() {
        while (true) {
            delay(Random.nextLong(2000, 6000))
            _state.update {
                it.copy(
                    leftEye = it.leftEye.copy(isBlinking = true),
                    rightEye = it.rightEye.copy(isBlinking = true)
                )
            }

            // Blink animation duration (~160ms)
            val steps = 10
            for (i in 0..steps) {
                val progress = if (i <= steps / 2) {
                    (i / (steps / 2f))
                } else {
                    1f - ((i - steps / 2f) / (steps / 2f))
                }
                _state.update {
                    it.copy(
                        leftEye = it.leftEye.copy(blinkProgress = progress),
                        rightEye = it.rightEye.copy(blinkProgress = progress)
                    )
                }
                delay(16)
            }

            _state.update {
                it.copy(
                    leftEye = it.leftEye.copy(isBlinking = false, blinkProgress = 0f),
                    rightEye = it.rightEye.copy(isBlinking = false, blinkProgress = 0f)
                )
            }
        }
    }

    private suspend fun startBreathingLoop() {
        var time = 0f
        while (true) {
            time += 0.05f
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
        morphToExpression(LuxExpression.SCANNING)
        _state.update { it.copy(isScanning = true) }
        for (i in 0..100) {
            _state.update { it.copy(scanProgress = i / 100f) }
            delay(20)
        }
        _state.update { it.copy(isScanning = false) }
        morphToExpression(LuxExpression.NEUTRAL)
    }

    private suspend fun runRefreshSequence() {
        _state.update { it.copy(isRefreshing = true, refreshProgress = 0f) }

        // Quick flicker/blank
        delay(100)

        // Vertical sweep reveal
        val steps = 40
        for (i in 0..steps) {
            _state.update { it.copy(refreshProgress = i / steps.toFloat()) }
            delay(16)
        }

        _state.update { it.copy(isRefreshing = false) }
    }

    private suspend fun morphToExpression(target: LuxExpression) {
        val startLeft = _state.value.leftEye.geometry
        val startRight = _state.value.rightEye.geometry
        val targetGeom = target.toGeometry()

        val duration = 300L
        val steps = 20
        val stepDelay = duration / steps

        for (i in 1..steps) {
            val fraction = i / steps.toFloat()
            _state.update {
                it.copy(
                    leftEye = it.leftEye.copy(
                        expression = target,
                        geometry = startLeft.lerp(targetGeom, fraction)
                    ),
                    rightEye = it.rightEye.copy(
                        expression = target,
                        geometry = startRight.lerp(targetGeom, fraction)
                    )
                )
            }
            delay(stepDelay)
        }
    }

    fun updateLookAt(x: Float, y: Float) {
        _state.update {
            it.copy(
                leftEye = it.leftEye.copy(lookAtX = x, lookAtY = y),
                rightEye = it.rightEye.copy(lookAtX = x, lookAtY = y)
            )
        }
    }
}
