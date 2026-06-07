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

    // Physics Solvers for different properties
    private val lookXSpring = SpringSolver(stiffness = 120f, dampingRatio = 0.6f)
    private val lookYSpring = SpringSolver(stiffness = 120f, dampingRatio = 0.6f)

    // Geometry morphing targets
    private var targetGeom = EyePresets.NEUTRAL
    private var targetLookX = 0f
    private var targetLookY = 0f

    // Flag to pause the idle loop during a high-priority reaction
    private var isReacting = false

    init {
        startBehaviors()
        startPhysicsTicker()
        startBreathingLoop()
    }

    private fun startBehaviors() {
        scope.launch { mainLoop() }
        scope.launch { startBlinkLoop() }
    }

    private fun startPhysicsTicker() {
        scope.launch {
            val dt = 0.016f // ~60fps
            while (true) {
                _state.update { currentState ->
                    val newLookX = lookXSpring.next(currentState.leftEye.lookAtX, targetLookX, dt)
                    val newLookY = lookYSpring.next(currentState.leftEye.lookAtY, targetLookY, dt)

                    // Simple LERP for geometry in this milestone, but driven by the ticker
                    val newGeom = currentState.leftEye.geometry.lerp(targetGeom, 0.15f)

                    currentState.copy(
                        leftEye = currentState.leftEye.copy(
                            lookAtX = newLookX,
                            lookAtY = newLookY,
                            geometry = newGeom
                        ),
                        rightEye = currentState.rightEye.copy(
                            lookAtX = newLookX + (newLookX * 0.03f), // subtle asymmetry
                            lookAtY = newLookY,
                            geometry = newGeom
                        )
                    )
                }
                delay(16)
            }
        }
    }

    private suspend fun mainLoop() {
        while (true) {
            if (!isReacting) {
                val intention = BehaviorSystem.getNextIntention(_state.value.mood)
                executeIntention(intention)
            }
            delay(BehaviorSystem.getNextBehaviorDelay())
        }
    }

    private suspend fun executeIntention(intention: BehaviorSystem.Intention) {
        when (intention) {
            BehaviorSystem.Intention.IDLE_OBSERVE -> {
                targetGeom = EyePresets.NEUTRAL
                targetLookX = Random.nextFloat() * 0.4f - 0.2f
                targetLookY = Random.nextFloat() * 0.4f - 0.2f
                delay(Random.nextLong(2000, 5000))
            }
            BehaviorSystem.Intention.INVESTIGATE -> {
                targetLookX = Random.nextFloat() * 1.4f - 0.7f
                targetLookY = Random.nextFloat() * 0.8f - 0.4f
                delay(800)
                targetGeom = EyePresets.FOCUSED
                delay(1200)
                targetGeom = EyePresets.CURIOUS
                delay(2000)
                targetGeom = EyePresets.NEUTRAL
            }
            BehaviorSystem.Intention.DROWSE -> {
                targetGeom = EyePresets.SLEEPY
                delay(Random.nextLong(4000, 7000))
                targetGeom = EyePresets.NEUTRAL
            }
            BehaviorSystem.Intention.SCAN_ENVIRONMENT -> {
                runScanSequence()
            }
            BehaviorSystem.Intention.REFRESH_DISPLAY -> {
                runRefreshSequence()
            }
        }
    }

    private suspend fun startBlinkLoop() {
        while (true) {
            delay(Random.nextLong(2500, 8000))
            _state.update { it.copy(
                leftEye = it.leftEye.copy(isBlinking = true),
                rightEye = it.rightEye.copy(isBlinking = true)
            )}

            for (i in 0..6) {
                val progress = if (i <= 3) i / 3f else 1f - (i - 3) / 3f
                _state.update { it.copy(
                    leftEye = it.leftEye.copy(blinkProgress = progress),
                    rightEye = it.rightEye.copy(blinkProgress = progress)
                )}
                delay(16)
            }

            _state.update { it.copy(
                leftEye = it.leftEye.copy(isBlinking = false, blinkProgress = 0f),
                rightEye = it.rightEye.copy(isBlinking = false, blinkProgress = 0f)
            )}
        }
    }

    private fun startBreathingLoop() {
        scope.launch {
            var time = 0f
            while (true) {
                time += 0.04f
                _state.update { it.copy(verticalOffset = sin(time) * 1.5f) }
                delay(32)
            }
        }
    }

    private suspend fun runScanSequence() {
        targetGeom = EyePresets.FOCUSED
        _state.update { it.copy(isScanning = true) }
        for (i in 0..60) {
            _state.update { it.copy(scanProgress = i / 60f) }
            delay(20)
        }
        _state.update { it.copy(isScanning = false) }
        targetGeom = EyePresets.NEUTRAL
    }

    private suspend fun runRefreshSequence() {
        _state.update { it.copy(isRefreshing = true, refreshProgress = 0f) }
        delay(100)
        for (i in 0..30) {
            _state.update { it.copy(refreshProgress = i / 30f) }
            delay(16)
        }
        _state.update { it.copy(isRefreshing = false) }
    }

    /**
     * Reacts to a user interaction (tap).
     */
    fun onInteraction() {
        if (isReacting) return

        scope.launch {
            isReacting = true

            // 1. Notice/Startle
            targetLookX = 0f
            targetLookY = -0.1f
            targetGeom = EyePresets.CURIOUS
            delay(400)

            // 2. Investigate/Focus
            targetGeom = EyePresets.FOCUSED
            delay(1500)

            // 3. Relax
            targetGeom = EyePresets.NEUTRAL
            delay(500)

            isReacting = false
        }
    }
}
