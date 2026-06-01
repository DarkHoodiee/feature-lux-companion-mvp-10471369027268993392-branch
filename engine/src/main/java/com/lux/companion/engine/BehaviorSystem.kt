package com.lux.companion.engine

import com.lux.companion.domain.LuxExpression
import com.lux.companion.domain.LuxMood
import kotlin.random.Random

object BehaviorSystem {

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
