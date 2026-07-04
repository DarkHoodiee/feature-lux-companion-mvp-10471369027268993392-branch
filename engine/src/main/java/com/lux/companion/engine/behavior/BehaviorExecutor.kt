package com.lux.companion.engine.behavior

import com.lux.companion.domain.EyePresets
import com.lux.companion.domain.StartupPhase
import com.lux.companion.engine.BehaviorSystem
import com.lux.companion.engine.animation.FaceAnimator
import com.lux.companion.domain.LuxFaceState
import com.lux.companion.engine.controllers.FaceController
import com.lux.companion.engine.attention.AttentionSystem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * Executes high-level behavioral intentions as sequences of animation targets.
 */
class BehaviorExecutor(
    private val scope: CoroutineScope,
    private val animator: FaceAnimator,
    private val stateFlow: MutableStateFlow<LuxFaceState>,
    private val attentionSystem: AttentionSystem
) {
    var isReacting = false
    var isStarted = false

    fun executeIntention(intention: BehaviorSystem.Intention) {
        scope.launch {
            when (intention) {
                BehaviorSystem.Intention.IDLE_OBSERVE -> {
                    animator.targetGeom = FaceController.getTargetGeometry(intention)

                    // If we have a specific interest point, look at it
                    val interest = attentionSystem.lastInterestPoint.value
                    if (interest != null) {
                        // Map screen coordinate to -1..1 range
                        animator.targetLookX = (interest.x / 500f - 1f).coerceIn(-0.8f, 0.8f)
                        animator.targetLookY = (interest.y / 800f - 1f).coerceIn(-0.6f, 0.6f)
                    } else {
                        animator.targetLookX = Random.nextFloat() * 0.4f - 0.2f
                        animator.targetLookY = Random.nextFloat() * 0.4f - 0.2f
                    }
                    delay(Random.nextLong(2000, 5000))
                }
                BehaviorSystem.Intention.INVESTIGATE -> {
                    // Similar logic for investigation, but with wider range
                    val interest = attentionSystem.lastInterestPoint.value
                    if (interest != null) {
                        animator.targetLookX = (interest.x / 500f - 1f).coerceIn(-1.2f, 1.2f)
                        animator.targetLookY = (interest.y / 800f - 1f).coerceIn(-0.8f, 0.8f)
                    } else {
                        animator.targetLookX = Random.nextFloat() * 1.4f - 0.7f
                        animator.targetLookY = Random.nextFloat() * 0.8f - 0.4f
                    }
                    delay(800)
                    animator.targetGeom = FaceController.getTargetGeometry(intention)
                    delay(1200)
                    animator.targetGeom = FaceController.getTargetGeometry(BehaviorSystem.Intention.IDLE_OBSERVE)
                }
                BehaviorSystem.Intention.DROWSE -> {
                    animator.targetGeom = FaceController.getTargetGeometry(intention)
                    delay(Random.nextLong(4000, 7000))
                    animator.targetGeom = FaceController.getTargetGeometry(BehaviorSystem.Intention.IDLE_OBSERVE)
                }
                BehaviorSystem.Intention.SCAN_ENVIRONMENT -> runScanSequence()
                BehaviorSystem.Intention.REFRESH_DISPLAY -> runRefreshSequence()
            }
        }
    }

    suspend fun runStartupSequence() {
        stateFlow.update { it.copy(startupPhase = StartupPhase.BOOT_DOT, startupProgress = 1.0f) }
        delay(200)
        stateFlow.update { it.copy(startupPhase = StartupPhase.EXPANSION_LINE) }
        for (i in 0..10) { stateFlow.update { it.copy(startupProgress = i / 10f) }; delay(15) }
        stateFlow.update { it.copy(startupPhase = StartupPhase.UPPER_SWEEP) }
        for (i in 0..20) { stateFlow.update { it.copy(startupProgress = i / 20f) }; delay(15) }
        stateFlow.update { it.copy(startupPhase = StartupPhase.LOWER_SWEEP) }
        for (i in 0..20) { stateFlow.update { it.copy(startupProgress = i / 20f) }; delay(15) }
        stateFlow.update { it.copy(startupPhase = StartupPhase.EYE_MATERIALIZING) }
        for (i in 0..30) { stateFlow.update { it.copy(startupProgress = i / 30f) }; delay(20) }
        stateFlow.update { it.copy(startupPhase = StartupPhase.ONLINE, startupProgress = 1.0f) }
        delay(100)
        isStarted = true
    }

    private suspend fun runScanSequence() {
        stateFlow.update { it.copy(isScanning = true) }
        animator.targetGeom = FaceController.getTargetGeometry(BehaviorSystem.Intention.SCAN_ENVIRONMENT)
        for (i in 0..60) { stateFlow.update { it.copy(scanProgress = i / 60f) }; delay(20) }
        stateFlow.update { it.copy(isScanning = false) }
        animator.targetGeom = FaceController.getTargetGeometry(BehaviorSystem.Intention.IDLE_OBSERVE)
    }

    private suspend fun runRefreshSequence() {
        stateFlow.update { it.copy(isRefreshing = true, refreshProgress = 0f) }
        delay(60)
        for (i in 0..10) { stateFlow.update { it.copy(refreshProgress = i / 10f) }; delay(10) }
        delay(40)
        stateFlow.update { it.copy(isRefreshing = false) }
    }

    fun handleInteraction() {
        if (!isStarted || isReacting) return
        scope.launch {
            isReacting = true
            // Look toward the last interest point during reaction
            val interest = attentionSystem.lastInterestPoint.value
            if (interest != null) {
                animator.targetLookX = (interest.x / 500f - 1f).coerceIn(-0.3f, 0.3f)
                animator.targetLookY = (interest.y / 800f - 1f).coerceIn(-0.2f, 0.2f)
            } else {
                animator.targetLookX = 0f
                animator.targetLookY = -0.1f
            }
            animator.targetGeom = EyePresets.CURIOUS
            delay(1000)
            animator.targetGeom = EyePresets.NEUTRAL
            delay(1500)
            isReacting = false
        }
    }
}
