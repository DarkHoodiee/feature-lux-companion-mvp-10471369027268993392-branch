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

    // Physics Solvers for movement
    private val lookXSpring = SpringSolver(stiffness = 120f, dampingRatio = 0.6f)
    private val lookYSpring = SpringSolver(stiffness = 120f, dampingRatio = 0.6f)

    // Physics Solvers for topology parameters
    private val widthScaleSpring = SpringSolver(stiffness = 180f, dampingRatio = 0.75f)
    private val heightScaleSpring = SpringSolver(stiffness = 180f, dampingRatio = 0.75f)
    private val upperCurveSpring = SpringSolver(stiffness = 150f, dampingRatio = 0.7f)
    private val lowerCurveSpring = SpringSolver(stiffness = 150f, dampingRatio = 0.7f)
    private val innerCompSpring = SpringSolver(stiffness = 150f, dampingRatio = 0.7f)
    private val outerExpSpring = SpringSolver(stiffness = 150f, dampingRatio = 0.7f)
    private val cornerPinchSpring = SpringSolver(stiffness = 120f, dampingRatio = 0.65f)
    private val taperSpring = SpringSolver(stiffness = 120f, dampingRatio = 0.65f)
    private val softnessSpring = SpringSolver(stiffness = 100f, dampingRatio = 0.8f)
    private val upperLidInsetSpring = SpringSolver(stiffness = 150f, dampingRatio = 0.75f)
    private val lowerLidInsetSpring = SpringSolver(stiffness = 150f, dampingRatio = 0.75f)

    private var targetTopology = EyeTopologyPresets.Neutral
    private var targetLookX = 0f
    private var targetLookY = 0f

    private var isReacting = false
    private var isBooted = false

    init {
        runStartupSequence()
        startPhysicsTicker()
        startBreathingLoop()
        startFloatingLoop()
    }

    private fun runStartupSequence() {
        scope.launch {
            // Phase 1: BOOT_DOT
            _state.update { it.copy(startupPhase = StartupPhase.BOOT_DOT, startupProgress = 0f) }
            delay(500)

            // Phase 2: EXPANSION_LINE
            _state.update { it.copy(startupPhase = StartupPhase.EXPANSION_LINE) }
            for (i in 0..20) {
                _state.update { it.copy(startupProgress = i / 20f) }
                delay(16)
            }
            delay(200)

            // Phase 3: UPPER_SWEEP
            _state.update { it.copy(startupPhase = StartupPhase.UPPER_SWEEP) }
            for (i in 0..25) {
                _state.update { it.copy(startupProgress = i / 25f) }
                delay(16)
            }

            // Phase 4: LOWER_SWEEP
            _state.update { it.copy(startupPhase = StartupPhase.LOWER_SWEEP) }
            for (i in 0..30) {
                _state.update { it.copy(startupProgress = i / 30f) }
                delay(16)
            }

            // Phase 5: EYE_MATERIALIZING
            _state.update { it.copy(startupPhase = StartupPhase.EYE_MATERIALIZING) }
            targetTopology = EyeTopologyPresets.Neutral
            for (i in 0..40) {
                val p = i / 40f
                _state.update { it.copy(
                    startupProgress = p,
                    leftEye = it.leftEye.copy(glowIntensity = p * 0.8f),
                    rightEye = it.rightEye.copy(glowIntensity = p * 0.8f)
                )}
                delay(16)
            }

            // Phase 6: ONLINE
            _state.update { it.copy(startupPhase = StartupPhase.ONLINE, startupProgress = 1f) }
            isBooted = true
            startBehaviors()
        }
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

                    val currentTopo = currentState.leftEye.topology

                    val newTopo = EyeTopology(
                        widthScale = widthScaleSpring.next(currentTopo.widthScale, targetTopology.widthScale, dt),
                        heightScale = heightScaleSpring.next(currentTopo.heightScale, targetTopology.heightScale, dt),
                        upperCurve = upperCurveSpring.next(currentTopo.upperCurve, targetTopology.upperCurve, dt),
                        lowerCurve = lowerCurveSpring.next(currentTopo.lowerCurve, targetTopology.lowerCurve, dt),
                        innerCompression = innerCompSpring.next(currentTopo.innerCompression, targetTopology.innerCompression, dt),
                        outerExpansion = outerExpSpring.next(currentTopo.outerExpansion, targetTopology.outerExpansion, dt),
                        cornerPinch = cornerPinchSpring.next(currentTopo.cornerPinch, targetTopology.cornerPinch, dt),
                        taper = taperSpring.next(currentTopo.taper, targetTopology.taper, dt),
                        softness = softnessSpring.next(currentTopo.softness, targetTopology.softness, dt),
                        upperLidInset = upperLidInsetSpring.next(currentTopo.upperLidInset, targetTopology.upperLidInset, dt),
                        lowerLidInset = lowerLidInsetSpring.next(currentTopo.lowerLidInset, targetTopology.lowerLidInset, dt)
                    )

                    currentState.copy(
                        leftEye = currentState.leftEye.copy(
                            lookAtX = newLookX,
                            lookAtY = newLookY,
                            topology = newTopo
                        ),
                        rightEye = currentState.rightEye.copy(
                            lookAtX = newLookX + (newLookX * 0.03f),
                            lookAtY = newLookY,
                            topology = newTopo
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
                targetTopology = EyeTopologyPresets.Neutral
                targetLookX = Random.nextFloat() * 0.4f - 0.2f
                targetLookY = Random.nextFloat() * 0.4f - 0.2f
                delay(Random.nextLong(2000, 5000))
            }
            BehaviorSystem.Intention.INVESTIGATE -> {
                targetLookX = Random.nextFloat() * 1.4f - 0.7f
                targetLookY = Random.nextFloat() * 0.8f - 0.4f
                delay(800)
                targetTopology = EyeTopologyPresets.Focused
                delay(1200)
                targetTopology = EyeTopologyPresets.Curious
                delay(2000)
                targetTopology = EyeTopologyPresets.Neutral
            }
            BehaviorSystem.Intention.DROWSE -> {
                targetTopology = EyeTopologyPresets.Sleepy
                delay(Random.nextLong(4000, 7000))
                targetTopology = EyeTopologyPresets.Neutral
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

    private fun startFloatingLoop() {
        scope.launch {
            var time = 0f
            while (true) {
                time += 0.02f
                val rotation = sin(time * 0.7f) * 2f
                _state.update { it.copy(rotationZ = rotation) }
                delay(32)
            }
        }
    }

    private suspend fun runScanSequence() {
        _state.update { it.copy(isScanning = true) }
        for (i in 0..100) {
            _state.update { it.copy(scanProgress = i / 100f) }
            delay(20)
        }
        _state.update { it.copy(isScanning = false) }
    }

    private suspend fun runRefreshSequence() {
        _state.update { it.copy(isRefreshing = true, refreshProgress = 0f) }
        delay(60)

        val steps = 10
        for (i in 0..steps) {
            _state.update { it.copy(refreshProgress = i / steps.toFloat()) }
            delay(10)
        }

        delay(40)
        _state.update { it.copy(isRefreshing = false) }
    }

    fun updateExpression(expression: LuxExpression) {
        if (!isBooted) return
        targetTopology = EyeTopologyPresets.forExpression(expression)
    }

    fun onInteraction() {
        if (isReacting || !isBooted) return

        scope.launch {
            isReacting = true
            targetLookX = 0f
            targetLookY = -0.1f
            targetTopology = EyeTopologyPresets.Curious
            delay(1000)
            targetTopology = EyeTopologyPresets.Focused
            delay(1500)
            targetTopology = EyeTopologyPresets.Neutral
            delay(500)
            isReacting = false
        }
    }
}
