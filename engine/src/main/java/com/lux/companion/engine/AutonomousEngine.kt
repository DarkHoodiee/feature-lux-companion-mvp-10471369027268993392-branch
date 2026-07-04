package com.lux.companion.engine

import com.lux.companion.domain.*
import com.lux.companion.domain.events.LuxEvent
import com.lux.companion.engine.animation.FaceAnimator
import com.lux.companion.engine.attention.AttentionSystem
import com.lux.companion.engine.attention.EventBus
import com.lux.companion.engine.behavior.BehaviorExecutor
import com.lux.companion.engine.behavior.BehaviorPlanner
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.math.sin
import kotlin.random.Random

/**
 * Evolutionary Thought-Driven Engine for LUX Hoodie.
 * Orchestrates Perception, Planning, and Execution.
 */
class AutonomousEngine(
    private val scope: CoroutineScope
) {
    private val _state = MutableStateFlow(LuxFaceState())
    val state: StateFlow<LuxFaceState> = _state.asStateFlow()

    private val animator = FaceAnimator(scope, _state)
    private val attention = AttentionSystem(scope)
    private val executor = BehaviorExecutor(scope, animator, _state, attention)
    private val planner = BehaviorPlanner(attention)

    init {
        scope.launch {
            executor.runStartupSequence()
            animator.start()
            startAutonomousProcesses()
            observeEvents()
        }
    }

    private fun startAutonomousProcesses() {
        scope.launch { mainPlanningLoop() }
        scope.launch { startSecondaryBehaviors() }
    }

    private suspend fun mainPlanningLoop() {
        while (true) {
            if (!executor.isReacting) {
                // Thought -> Decision
                val intention = planner.planNextIntention(_state.value.mood)
                // Decision -> Behavior
                executor.executeIntention(intention)
            }
            delay(BehaviorSystem.getNextBehaviorDelay())
        }
    }

    private fun observeEvents() {
        EventBus.events.onEach { event ->
            // Perception -> Context
            attention.processEvent(event)

            // Reaction Logic
            when (event) {
                is LuxEvent.Interaction -> executor.handleInteraction()
                else -> {}
            }
        }.launchIn(scope)
    }

    private fun startSecondaryBehaviors() {
        // Breathing
        scope.launch {
            var time = 0f
            while (true) {
                time += 0.04f
                _state.update { it.copy(verticalOffset = sin(time) * 1.5f) }
                delay(32)
            }
        }
        // Floating
        scope.launch {
            var time = 0f
            while (true) {
                time += 0.02f
                _state.update { it.copy(rotationZ = sin(time * 0.7f) * 2f) }
                delay(32)
            }
        }
        // Blinking
        scope.launch {
            while (true) {
                delay(Random.nextLong(2000, 6000))
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
    }

    fun updateExpression(expression: LuxExpression) {
        animator.targetGeom = expression.toGeometry()
    }

    fun updateLookAt(x: Float, y: Float) {
        animator.targetLookX = x
        animator.targetLookY = y
    }

    fun onInteraction(interaction: LuxInteraction) {
        scope.launch {
            val event = when (interaction) {
                is LuxInteraction.Tap -> LuxEvent.Touch(interaction.x, interaction.y)
                is LuxInteraction.DoubleTap -> LuxEvent.Interaction(com.lux.companion.domain.events.InteractionType.DOUBLE_TAP)
                is LuxInteraction.LongPress -> LuxEvent.Interaction(com.lux.companion.domain.events.InteractionType.LONG_PRESS)
                is LuxInteraction.RapidTaps -> LuxEvent.Interaction(com.lux.companion.domain.events.InteractionType.TAP)
            }
            EventBus.publish(event)
            // For immediate feedback
            if (event is LuxEvent.Touch) {
                EventBus.publish(LuxEvent.Interaction(com.lux.companion.domain.events.InteractionType.TAP))
            }
        }
    }
}
