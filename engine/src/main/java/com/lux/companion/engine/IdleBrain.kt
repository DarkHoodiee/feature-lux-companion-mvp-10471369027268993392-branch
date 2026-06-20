package com.lux.companion.engine

import com.lux.companion.domain.LuxMood
import kotlin.random.Random

/**
 * IdleBrain.kt
 *
 * Logic-only behavior selection for the digital companion.
 * Contains no rendering, no animations, and no Compose logic.
 */
object IdleBrain {

    enum class Action {
        NEUTRAL_OBSERVATION,
        LOOK_LEFT,
        LOOK_RIGHT,
        LOOK_UP,
        CURIOSITY_TILT,
        FIXATION,
        SCREEN_REFRESH,
        MICRO_ADJUSTMENT
    }

    fun selectNextAction(mood: LuxMood): Action {
        val rand = Random.nextInt(100)
        return when {
            rand < 40 -> Action.NEUTRAL_OBSERVATION
            rand < 55 -> Action.LOOK_LEFT
            rand < 70 -> Action.LOOK_RIGHT
            rand < 80 -> Action.LOOK_UP
            rand < 90 -> Action.CURIOSITY_TILT
            rand < 95 -> Action.FIXATION
            rand < 98 -> Action.SCREEN_REFRESH
            else -> Action.MICRO_ADJUSTMENT
        }
    }

    fun getActionDuration(action: Action): Long {
        return when (action) {
            Action.NEUTRAL_OBSERVATION -> Random.nextLong(3000, 6000)
            Action.LOOK_LEFT, Action.LOOK_RIGHT, Action.LOOK_UP -> Random.nextLong(1500, 3500)
            Action.CURIOSITY_TILT -> Random.nextLong(2000, 4000)
            Action.FIXATION -> Random.nextLong(2500, 5000)
            Action.SCREEN_REFRESH -> 500L
            Action.MICRO_ADJUSTMENT -> Random.nextLong(400, 1000)
        }
    }
}
