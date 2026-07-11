package com.lux.companion.engine.animation

import com.lux.companion.domain.EyeTopology
import com.lux.companion.domain.LuxFaceState
import com.lux.companion.engine.SpringSolver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Orchestrates physics-based transitions for LUX's face.
 * Drives EyeTopology and spatial parameters using SpringSolver.
 */
class FaceAnimator(
    private val scope: CoroutineScope,
    private val state: MutableStateFlow<LuxFaceState>
) {
    private val tickerPeriod = 16L // ~60fps
    private val dt = tickerPeriod / 1000f

    // Targets
    var targetTopology: EyeTopology = EyeTopology()
    var targetLookX = 0f
    var targetLookY = 0f
    var targetRotationZ = 0f

    // Spring Solvers for Topology parameters
    private val widthSpring = SpringSolver(stiffness = 150f, dampingRatio = 0.7f)
    private val heightSpring = SpringSolver(stiffness = 150f, dampingRatio = 0.7f)
    private val upperCurveSpring = SpringSolver(stiffness = 120f, dampingRatio = 0.6f)
    private val lowerCurveSpring = SpringSolver(stiffness = 120f, dampingRatio = 0.6f)
    private val innerCompSpring = SpringSolver(stiffness = 140f, dampingRatio = 0.75f)
    private val outerExpSpring = SpringSolver(stiffness = 140f, dampingRatio = 0.75f)
    private val cornerPinchSpring = SpringSolver(stiffness = 180f, dampingRatio = 0.8f)
    private val taperSpring = SpringSolver(stiffness = 130f, dampingRatio = 0.7f)
    private val softnessSpring = SpringSolver(stiffness = 100f, dampingRatio = 0.9f)
    private val upperLidSpring = SpringSolver(stiffness = 200f, dampingRatio = 0.85f)
    private val lowerLidSpring = SpringSolver(stiffness = 200f, dampingRatio = 0.85f)
    private val tiltSpring = SpringSolver(stiffness = 110f, dampingRatio = 0.7f)

    // Spatial Springs
    private val lookXSpring = SpringSolver(stiffness = 180f, dampingRatio = 0.8f)
    private val lookYSpring = SpringSolver(stiffness = 180f, dampingRatio = 0.8f)
    private val rotationSpring = SpringSolver(stiffness = 90f, dampingRatio = 0.6f)

    fun start() {
        scope.launch {
            while (isActive) {
                state.update { current ->
                    val currentGeom = current.leftEye.topology

                    val nextTopology = EyeTopology(
                        widthScale = widthSpring.next(currentGeom.widthScale, targetTopology.widthScale, dt),
                        heightScale = heightSpring.next(currentGeom.heightScale, targetTopology.heightScale, dt),
                        upperCurve = upperCurveSpring.next(currentGeom.upperCurve, targetTopology.upperCurve, dt),
                        lowerCurve = lowerCurveSpring.next(currentGeom.lowerCurve, targetTopology.lowerCurve, dt),
                        innerCompression = innerCompSpring.next(currentGeom.innerCompression, targetTopology.innerCompression, dt),
                        outerExpansion = outerExpSpring.next(currentGeom.outerExpansion, targetTopology.outerExpansion, dt),
                        cornerPinch = cornerPinchSpring.next(currentGeom.cornerPinch, targetTopology.cornerPinch, dt),
                        taper = taperSpring.next(currentGeom.taper, targetTopology.taper, dt),
                        softness = softnessSpring.next(currentGeom.softness, targetTopology.softness, dt),
                        upperLidInset = upperLidSpring.next(currentGeom.upperLidInset, targetTopology.upperLidInset, dt),
                        lowerLidInset = lowerLidSpring.next(currentGeom.lowerLidInset, targetTopology.lowerLidInset, dt),
                        tilt = tiltSpring.next(currentGeom.tilt, targetTopology.tilt, dt)
                    )

                    current.copy(
                        leftEye = current.leftEye.copy(
                            topology = nextTopology,
                            lookAtX = lookXSpring.next(current.leftEye.lookAtX, targetLookX, dt),
                            lookAtY = lookYSpring.next(current.leftEye.lookAtY, targetLookY, dt)
                        ),
                        rightEye = current.rightEye.copy(
                            topology = nextTopology,
                            lookAtX = lookXSpring.next(current.rightEye.lookAtX, targetLookX, dt),
                            lookAtY = lookYSpring.next(current.rightEye.lookAtY, targetLookY, dt)
                        ),
                        rotationZ = rotationSpring.next(current.rotationZ, targetRotationZ, dt)
                    )
                }
                delay(tickerPeriod)
            }
        }
    }
}
