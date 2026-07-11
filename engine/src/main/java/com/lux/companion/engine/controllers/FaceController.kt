package com.lux.companion.engine.controllers

import com.lux.companion.domain.EyeTopology
import com.lux.companion.domain.LuxExpression
import com.lux.companion.domain.toTopology
import com.lux.companion.engine.BehaviorSystem

/**
 * Translates behavioral Intentions into physical EyeTopology and Expression states.
 */
object FaceController {

    fun getExpression(intention: BehaviorSystem.Intention): LuxExpression {
        return when (intention) {
            BehaviorSystem.Intention.IDLE_OBSERVE -> LuxExpression.NEUTRAL
            BehaviorSystem.Intention.INVESTIGATE -> LuxExpression.CURIOUS
            BehaviorSystem.Intention.DROWSE -> LuxExpression.SLEEPY
            BehaviorSystem.Intention.SCAN_ENVIRONMENT -> LuxExpression.SCANNING
            BehaviorSystem.Intention.REFRESH_DISPLAY -> LuxExpression.NEUTRAL
        }
    }

    fun getTopology(intention: BehaviorSystem.Intention): EyeTopology {
        return getExpression(intention).toTopology()
    }
}
