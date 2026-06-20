package com.lux.companion.engine

import com.lux.companion.domain.LuxMood
import com.lux.companion.domain.LuxExpression
import org.junit.Test
import org.junit.Assert.*

class IdleBrainTest {

    @Test
    fun testSelectNextAction() {
        val action = IdleBrain.selectNextAction(LuxMood.CALM)
        assertNotNull(action)
    }

    @Test
    fun testGetActionDuration() {
        val duration = IdleBrain.getActionDuration(IdleBrain.Action.NEUTRAL_OBSERVATION)
        assertTrue(duration >= 3000)
    }
}
