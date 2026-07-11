package com.lux.companion.engine.behavior

import com.lux.companion.domain.LuxFaceState
import com.lux.companion.domain.StartupPhase
import com.lux.companion.engine.animation.FaceAnimator
import com.lux.companion.engine.attention.AttentionSystem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Executes high-level Intentions by sequencing animation targets and timing.
 */
class BehaviorExecutor(
    private val scope: CoroutineScope,
    private val animator: FaceAnimator,
    private val state: MutableStateFlow<LuxFaceState>,
    private val attention: AttentionSystem
) {
    var isReacting = false
        private set

    fun executeIntention(intention: com.lux.companion.engine.BehaviorSystem.Intention) {
        if (isReacting) return

        when (intention) {
            com.lux.companion.engine.BehaviorSystem.Intention.IDLE_OBSERVE -> runIdleObserve()
            com.lux.companion.engine.BehaviorSystem.Intention.INVESTIGATE -> runInvestigate()
            com.lux.companion.engine.BehaviorSystem.Intention.DROWSE -> runDrowse()
            com.lux.companion.engine.BehaviorSystem.Intention.SCAN_ENVIRONMENT -> runScan()
            com.lux.companion.engine.BehaviorSystem.Intention.REFRESH_DISPLAY -> runRefresh()
        }
    }

    suspend fun runStartupSequence() {
        isReacting = true

        updateStartup(StartupPhase.CENTER_DOT, 0f)
        delay(800)

        updateStartup(StartupPhase.HORIZONTAL_EXPANSION, 1f, duration = 600)
        updateStartup(StartupPhase.SWEEP_UP_TOP, 1f, duration = 800)

        delay(400)
        updateStartup(StartupPhase.NEW_LINE_BOTTOM, 1f, duration = 500)
        updateStartup(StartupPhase.SWEEP_UP_REVEAL, 1f, duration = 1200)

        state.update { it.copy(startupPhase = StartupPhase.ONLINE, startupProgress = 1f) }
        isReacting = false
    }

    private suspend fun updateStartup(phase: StartupPhase, target: Float, duration: Long = 0) {
        state.update { it.copy(startupPhase = phase, startupProgress = 0f) }
        if (duration > 0) {
            val steps = (duration / 16).toInt()
            for (i in 1..steps) {
                state.update { it.copy(startupProgress = i.toFloat() / steps) }
                delay(16)
            }
        }
    }

    private fun runIdleObserve() {
        // Logic for idle looking around
    }

    private fun runInvestigate() {
        // Logic for curious tilting
    }

    private fun runDrowse() {
        // Logic for heavy lids
    }

    private fun runScan() {
        scope.launch {
            state.update { it.copy(isScanning = true, scanProgress = 0f) }
            for (i in 0..100) {
                state.update { it.copy(scanProgress = i / 100f) }
                delay(20)
            }
            state.update { it.copy(isScanning = false) }
        }
    }

    private fun runRefresh() {
        scope.launch {
            state.update { it.copy(isRefreshing = true, refreshProgress = 0f) }
            for (i in 0..100) {
                state.update { it.copy(refreshProgress = i / 100f) }
                delay(10)
            }
            state.update { it.copy(isRefreshing = false) }
        }
    }

    fun handleInteraction() {
        // Handle touch events
    }
}
