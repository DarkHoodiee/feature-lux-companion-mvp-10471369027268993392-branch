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
    private val _state = MutableStateFlow(LuxFaceState(startupPhase = StartupPhase.OFF))
    val state: StateFlow<LuxFaceState> = _state.asStateFlow()

    private val lookXSpring = SpringSolver(stiffness = 100f, dampingRatio = 0.65f)
    private val lookYSpring = SpringSolver(stiffness = 100f, dampingRatio = 0.65f)

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
    private var isInitialized = false

    init {
        scope.launch {
            runStartupSequence()
            isInitialized = true
            startBehaviors()
            startPhysicsTicker()
        }
    }

    private suspend fun runStartupSequence() {
        // Phase 1: Center Dot
        _state.update { it.copy(startupPhase = StartupPhase.CENTER_DOT, startupProgress = 0f) }
        delay(200)

        // Phase 2: Expand Line
        _state.update { it.copy(startupPhase = StartupPhase.EXPAND_LINE) }
        for (i in 0..10) {
            _state.update { it.copy(startupProgress = i / 10f) }
            delay(20)
        }

        // Phase 3: Sweep Up Disappear (Center to Top)
        _state.update { it.copy(startupPhase = StartupPhase.SWEEP_UP_DISAPPEAR) }
        for (i in 0..15) {
            _state.update { it.copy(startupProgress = i / 15f) }
            delay(15)
        }

        // Phase 4: Bottom Line Appear & Sweep Up (Bottom to Center)
        _state.update { it.copy(startupPhase = StartupPhase.BOTTOM_LINE_APPEAR) }
        for (i in 0..20) {
            _state.update { it.copy(startupProgress = i / 20f) }
            delay(15)
        }

        // Phase 5: Sweep reaches center -> Eyes materialize & Glow ramps in
        _state.update { it.copy(startupPhase = StartupPhase.SWEEP_UP_REVEAL) }
        for (i in 0..25) {
            val progress = i / 25f
            _state.update {
                it.copy(
                    startupProgress = progress,
                    leftEye = it.leftEye.copy(glowIntensity = progress),
                    rightEye = it.rightEye.copy(glowIntensity = progress)
                )
            }
            delay(15)
        }

        // Phase 6: Complete
        _state.update { it.copy(startupPhase = StartupPhase.COMPLETE) }
    }

    private fun startBehaviors() {
        scope.launch { mainLoop() }
        scope.launch { startBlinkLoop() }
        scope.launch { startBreathingLoop() }
        scope.launch { startFloatingLoop() }
    }

    private fun startPhysicsTicker() {
        scope.launch {
            val dt = 0.016f
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
                val action = IdleBrain.selectNextAction(_state.value.mood)
                executeAction(action)
                delay(IdleBrain.getActionDuration(action))
            } else {
                delay(100)
            }
        }
    }

    private suspend fun executeAction(action: IdleBrain.Action) {
        when (action) {
            IdleBrain.Action.NEUTRAL_OBSERVATION -> {
                targetGeom = EyePresets.NEUTRAL
                targetLookX = Random.nextFloat() * 0.2f - 0.1f
                targetLookY = Random.nextFloat() * 0.2f - 0.1f
            }
            IdleBrain.Action.LOOK_LEFT -> {
                targetLookX = -0.6f
                targetLookY = Random.nextFloat() * 0.3f - 0.15f
                delay(400)
                targetLookX -= 0.1f
            }
            IdleBrain.Action.LOOK_RIGHT -> {
                targetLookX = 0.6f
                targetLookY = Random.nextFloat() * 0.3f - 0.15f
                delay(400)
                targetLookX += 0.1f
            }
            IdleBrain.Action.LOOK_UP -> {
                targetLookY = -0.5f
                targetLookX = Random.nextFloat() * 0.4f - 0.2f
            }
            IdleBrain.Action.CURIOSITY_TILT -> {
                targetGeom = EyePresets.CURIOUS
                targetLookX = Random.nextFloat() * 0.4f - 0.2f
                targetLookY = -0.2f
            }
            IdleBrain.Action.FIXATION -> {
                targetGeom = EyePresets.FOCUSED
                targetLookX = 0f
                targetLookY = 0f
            }
            IdleBrain.Action.SCREEN_REFRESH -> {
                runRefreshSequence()
            }
            IdleBrain.Action.MICRO_ADJUSTMENT -> {
                targetLookX += (Random.nextFloat() * 0.1f - 0.05f)
                targetLookY += (Random.nextFloat() * 0.1f - 0.05f)
            }
        }
    }

    private suspend fun startBlinkLoop() {
        while (true) {
            delay(Random.nextLong(4000, 10000))
            if (_state.value.isRefreshing || _state.value.startupPhase != StartupPhase.COMPLETE) continue

            val steps = 6
            for (i in 0..steps) {
                val progress = if (i <= steps / 2) i / (steps / 2f) else 1f - (i - steps / 2) / (steps / 2f)
                _state.update {
                    it.copy(
                        leftEye = it.leftEye.copy(isBlinking = true, blinkProgress = progress),
                        rightEye = it.rightEye.copy(isBlinking = true, blinkProgress = progress)
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
            time += 0.04f
            _state.update { it.copy(verticalOffset = sin(time) * 1.2f) }
            delay(32)
        }
    }

    private suspend fun startFloatingLoop() {
        var time = 0f
        while (true) {
            time += 0.015f
            val rotation = sin(time * 0.6f) * 1.5f
            _state.update { it.copy(rotationZ = rotation) }
            delay(32)
        }
    }

    private suspend fun runRefreshSequence() {
        _state.update { it.copy(isRefreshing = true, refreshProgress = 0f) }
        delay(120)
        val steps = 12
        for (i in 0..steps) {
            _state.update { it.copy(refreshProgress = i / steps.toFloat()) }
            delay(12)
        }
        delay(60)
        _state.update { it.copy(isRefreshing = false) }
    }

    fun onInteraction() {
        if (isReacting || !isInitialized) return

        scope.launch {
            isReacting = true
            val prevGeom = targetGeom
            targetLookX = 0f
            targetLookY = -0.1f
            targetGeom = EyePresets.CURIOUS
            delay(1200)
            targetGeom = EyePresets.FOCUSED
            delay(1500)
            targetGeom = prevGeom
            delay(500)
            isReacting = false
        }
    }
}
