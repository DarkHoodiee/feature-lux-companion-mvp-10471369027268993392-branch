package com.lux.companion.interaction

import com.lux.companion.domain.LuxExpression
import com.lux.companion.domain.LuxInteraction

class InteractionHandler(
    private val onExpressionChange: (LuxExpression) -> Unit
) {
    fun handleInteraction(interaction: LuxInteraction) {
        when (interaction) {
            is LuxInteraction.Tap -> onExpressionChange(LuxExpression.CURIOUS)
            is LuxInteraction.DoubleTap -> onExpressionChange(LuxExpression.HAPPY)
            is LuxInteraction.LongPress -> onExpressionChange(LuxExpression.SLEEPY)
            is LuxInteraction.RapidTaps -> onExpressionChange(LuxExpression.EXCITED)
        }
    }
}
