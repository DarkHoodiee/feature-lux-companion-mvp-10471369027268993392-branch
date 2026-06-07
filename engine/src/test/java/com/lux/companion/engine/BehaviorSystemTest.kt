package com.lux.companion.engine

import com.lux.companion.domain.LuxMood
import com.lux.companion.domain.LuxExpression
import org.junit.Test
import org.junit.Assert.*

class BehaviorSystemTest {

    @Test
    fun testGetRandomExpression() {
        val expression = BehaviorSystem.getRandomExpression(LuxMood.CALM)
        assertNotNull(expression)
        assertTrue(expression is LuxExpression)
    }

    @Test
    fun testGetNextBehaviorDelay() {
        val delay = BehaviorSystem.getNextBehaviorDelay()
        assertTrue(delay in 3000..8000)
    }
}
