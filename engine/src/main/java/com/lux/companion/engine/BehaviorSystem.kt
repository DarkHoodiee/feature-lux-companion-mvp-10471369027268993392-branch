package com.lux.companion.engine

import com.lux.companion.domain.LuxExpression
import com.lux.companion.domain.LuxMood
import kotlin.random.Random

object BehaviorSystem {

/**
 * Higher-level behavior logic.
 * Moves away from random weighted selection toward "Intentional Sequences".
 */
object BehaviorSystem {

    enum class Intention {
        IDLE_OBSERVE,
        INVESTIGATE,
        DROWSE,
        REFRESH_DISPLAY,
        SCAN_ENVIRONMENT
    }

    fun getNextIntention(currentMood: LuxMood): Intention {
        val rand = Random.nextFloat()
        return when {
            rand < 0.60f -> Intention.IDLE_OBSERVE
            rand < 0.80f -> Intention.INVESTIGATE
            rand < 0.93f -> Intention.DROWSE
            rand < 0.97f -> Intention.SCAN_ENVIRONMENT
            else -> Intention.REFRESH_DISPLAY
        }
    }

    fun getNextBehaviorDelay(): Long {
        return Random.nextLong(3000, 8000)
    }

    // Legacy support for simple randoms if needed
    private val weights = mapOf(
        LuxExpression.NEUTRAL to 60,
        LuxExpression.CURIOUS to 15,
        LuxExpression.THINKING to 10,
        LuxExpression.FOCUSED to 7,
        LuxExpression.HAPPY to 5,
        LuxExpression.SLEEPY to 3
    )

    fun getRandomExpression(currentMood: LuxMood): LuxExpression {
        val totalWeight = weights.values.sum()
        var random = Random.nextInt(totalWeight)

        for ((expression, weight) in weights) {
            if (random < weight) return expression
            random -= weight
        }
        return LuxExpression.NEUTRAL
    }

    fun getNextBehaviorDelay(): Long {
        return Random.nextLong(3000, 8000)
    }
}
