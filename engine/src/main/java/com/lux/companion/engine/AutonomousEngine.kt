package com.lux.companion.engine

import com.lux.companion.domain.LuxFaceState
import com.lux.companion.domain.LuxMood
import com.lux.companion.domain.LuxExpression
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
    // Physics Solvers for movement
    private val lookXSpring = SpringSolver(stiffness = 120f, dampingRatio = 0.6f)
    private val lookYSpring = SpringSolver(stiffness = 120f, dampingRatio = 0.6f)

    // Physics Solvers for geometry parameters
    private val widthSpring = SpringSolver(stiffness = 180f, dampingRatio = 0.75f)
    private val heightSpring = SpringSolver(stiffness = 180f, dampingRatio = 0.75f)
    private val upperCurveSpring = SpringSolver(stiffness = 150f, dampingRatio = 0.7f)
    private val lowerCurveSpring = SpringSolver(stiffness = 150f, dampingRatio = 0.7f)
    private val innerCompSpring = SpringSolver(stiffness = 150f, dampingRatio = 0.7f)
    private val outerCompSpring = SpringSolver(stiffness = 150f, dampingRatio = 0.7f)
    private val tiltSpring = SpringSolver(stiffness = 120f, dampingRatio = 0.65f)
    private val shearSpring = SpringSolver(stiffness = 120f, dampingRatio = 0.65f)

    private var targetGeom = EyePresets.NEUTRAL
    private var targetLookX = 0f
    private var targetLookY = 0f

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

                    val currentGeom = currentState.leftEye.geometry

                    val newGeom = EyeGeometry(
                        width = widthSpring.next(currentGeom.width, targetGeom.width, dt),
                        height = heightSpring.next(currentGeom.height, targetGeom.height, dt),
                        upperCurve = upperCurveSpring.next(currentGeom.upperCurve, targetGeom.upperCurve, dt),
                        lowerCurve = lowerCurveSpring.next(currentGeom.lowerCurve, targetGeom.lowerCurve, dt),
                        innerCompression = innerCompSpring.next(currentGeom.innerCompression, targetGeom.innerCompression, dt),
                        outerCompression = outerCompSpring.next(currentGeom.outerCompression, targetGeom.outerCompression, dt),
                        tilt = tiltSpring.next(currentGeom.tilt, targetGeom.tilt, dt),
                        shear = shearSpring.next(currentGeom.shear, targetGeom.shear, dt)
                    )

                    currentState.copy(
                        leftEye = currentState.leftEye.copy(
                            lookAtX = newLookX,
                            lookAtY = newLookY,
                            geometry = newGeom
                        ),
                        rightEye = currentState.rightEye.copy(
                            lookAtX = newLookX + (newLookX * 0.03f),
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
            else -> {}
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
        // Fast hardware style refresh: 200ms total
        _state.update { it.copy(isRefreshing = true, refreshProgress = 0f) }
        delay(60) // Eyes disappear

        val steps = 10
        for (i in 0..steps) {
            _state.update { it.copy(refreshProgress = i / steps.toFloat()) }
            delay(10) // Rapid sweep
        }

        delay(40) // Pause at bottom
        _state.update { it.copy(isRefreshing = false) }
    }

    fun onInteraction() {
        if (isReacting) return

        scope.launch {
            isReacting = true
            targetLookX = 0f
            targetLookY = -0.1f
            targetGeom = EyePresets.CURIOUS
            delay(1000)
            targetGeom = EyePresets.FOCUSED
            delay(1500)
            targetGeom = EyePresets.NEUTRAL
            delay(500)
            isReacting = false
        }
    }
}
