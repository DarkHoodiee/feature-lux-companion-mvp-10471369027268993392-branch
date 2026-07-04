package com.lux.companion.engine.behavior

import com.lux.companion.domain.LuxMood
import com.lux.companion.domain.behavior.InternalMotivation
import com.lux.companion.engine.BehaviorSystem
import com.lux.companion.engine.attention.AttentionSystem
import kotlin.random.Random

/**
 * Thought Layer: Evaluates Attention and Context to produce Intentions.
 */
class BehaviorPlanner(
    private val attentionSystem: AttentionSystem
) {
    fun planNextIntention(currentMood: LuxMood): BehaviorSystem.Intention {
        val focus = attentionSystem.focusLevel.value
        val motivation = evaluateMotivation(focus)
        val rand = Random.nextFloat()

        return when (motivation) {
            InternalMotivation.VIGILANCE -> {
                // High focus favors investigation of the interest point
                if (rand < 0.8f) BehaviorSystem.Intention.INVESTIGATE
                else BehaviorSystem.Intention.IDLE_OBSERVE
            }
            InternalMotivation.CURIOSITY -> {
                // Moderate interest favors general observation or investigation
                if (rand < 0.5f) BehaviorSystem.Intention.INVESTIGATE
                else BehaviorSystem.Intention.IDLE_OBSERVE
            }
            InternalMotivation.REST -> {
                // Low interest favors drowsing and refreshing
                when {
                    rand < 0.3f -> BehaviorSystem.Intention.IDLE_OBSERVE
                    rand < 0.7f -> BehaviorSystem.Intention.DROWSE
                    else -> BehaviorSystem.Intention.REFRESH_DISPLAY
                }
            }
            InternalMotivation.BOREDOM -> {
                // Neutral state favors scanning or looking around
                if (rand < 0.4f) BehaviorSystem.Intention.SCAN_ENVIRONMENT
                else BehaviorSystem.Intention.IDLE_OBSERVE
            }
            else -> BehaviorSystem.Intention.IDLE_OBSERVE
        }
    }

    private fun evaluateMotivation(focus: Float): InternalMotivation {
        return when {
            focus > 0.75f -> InternalMotivation.VIGILANCE
            focus > 0.35f -> InternalMotivation.CURIOSITY
            focus < 0.15f -> InternalMotivation.REST
            else -> InternalMotivation.BOREDOM
        }
    }
}
