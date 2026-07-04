package com.lux.companion.engine.animation

import com.lux.companion.domain.EyeGeometry
import com.lux.companion.domain.EyePresets
import com.lux.companion.domain.EyeState
import com.lux.companion.domain.LuxFaceState
import com.lux.companion.engine.SpringSolver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Manages the physics-based animation ticker for LUX.
 * Decouples spring simulation from behavioral logic.
 */
class FaceAnimator(
    private val scope: CoroutineScope,
    private val stateFlow: MutableStateFlow<LuxFaceState>
) {
    private val lookXSpring = SpringSolver(stiffness = 120f, dampingRatio = 0.6f)
    private val lookYSpring = SpringSolver(stiffness = 120f, dampingRatio = 0.6f)

    private val widthSpring = SpringSolver(stiffness = 180f, dampingRatio = 0.75f)
    private val heightSpring = SpringSolver(stiffness = 180f, dampingRatio = 0.75f)
    private val upperCurveSpring = SpringSolver(stiffness = 150f, dampingRatio = 0.7f)
    private val lowerCurveSpring = SpringSolver(stiffness = 150f, dampingRatio = 0.7f)
    private val innerTaperSpring = SpringSolver(stiffness = 150f, dampingRatio = 0.7f)
    private val outerExpansionSpring = SpringSolver(stiffness = 150f, dampingRatio = 0.7f)
    private val softnessSpring = SpringSolver(stiffness = 150f, dampingRatio = 0.7f)
    private val tiltSpring = SpringSolver(stiffness = 120f, dampingRatio = 0.65f)
    private val shearSpring = SpringSolver(stiffness = 120f, dampingRatio = 0.65f)

    var targetGeom: EyeGeometry = EyePresets.NEUTRAL
    var targetLookX: Float = 0f
    var targetLookY: Float = 0f

    fun start() {
        scope.launch {
            val dt = 0.016f // ~60fps
            while (true) {
                stateFlow.update { currentState ->
                    val newLookX = lookXSpring.next(currentState.leftEye.lookAtX, targetLookX, dt)
                    val newLookY = lookYSpring.next(currentState.leftEye.lookAtY, targetLookY, dt)

                    val currentGeom = currentState.leftEye.geometry
                    val newGeom = EyeGeometry(
                        width = widthSpring.next(currentGeom.width, targetGeom.width, dt),
                        height = heightSpring.next(currentGeom.height, targetGeom.height, dt),
                        upperCurve = upperCurveSpring.next(currentGeom.upperCurve, targetGeom.upperCurve, dt),
                        lowerCurve = lowerCurveSpring.next(currentGeom.lowerCurve, targetGeom.lowerCurve, dt),
                        innerTaper = innerTaperSpring.next(currentGeom.innerTaper, targetGeom.innerTaper, dt),
                        outerExpansion = outerExpansionSpring.next(currentGeom.outerExpansion, targetGeom.outerExpansion, dt),
                        softness = softnessSpring.next(currentGeom.softness, targetGeom.softness, dt),
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
}
