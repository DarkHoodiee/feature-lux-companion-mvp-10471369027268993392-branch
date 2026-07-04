package com.lux.companion.engine.controllers

import com.lux.companion.domain.EyeGeometry
import com.lux.companion.domain.EyePresets
import com.lux.companion.domain.LuxExpression
import com.lux.companion.domain.toGeometry
import com.lux.companion.engine.BehaviorSystem

/**
 * Translates abstract Behavioral Intentions into concrete geometry and expression targets.
 */
object FaceController {

    fun getTargetGeometry(intention: BehaviorSystem.Intention): EyeGeometry {
        return when (intention) {
            BehaviorSystem.Intention.IDLE_OBSERVE -> EyePresets.NEUTRAL
            BehaviorSystem.Intention.INVESTIGATE -> EyePresets.FOCUSED
            BehaviorSystem.Intention.DROWSE -> EyePresets.SLEEPY
            BehaviorSystem.Intention.SCAN_ENVIRONMENT -> EyePresets.FOCUSED
            BehaviorSystem.Intention.REFRESH_DISPLAY -> EyePresets.NEUTRAL
        }
    }

    fun getExpression(intention: BehaviorSystem.Intention): LuxExpression {
        return when (intention) {
            BehaviorSystem.Intention.IDLE_OBSERVE -> LuxExpression.NEUTRAL
            BehaviorSystem.Intention.INVESTIGATE -> LuxExpression.CURIOUS
            BehaviorSystem.Intention.DROWSE -> LuxExpression.SLEEPY
            BehaviorSystem.Intention.SCAN_ENVIRONMENT -> LuxExpression.SCANNING
            BehaviorSystem.Intention.REFRESH_DISPLAY -> LuxExpression.NEUTRAL
        }
    }
}
