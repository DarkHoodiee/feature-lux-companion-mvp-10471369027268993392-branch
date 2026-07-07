package com.lux.hoodie.behavior

import com.lux.hoodie.domain.GestureEvent
import com.lux.hoodie.domain.GestureType
import org.junit.Assert.assertTrue
import org.junit.Test

class BehaviorPlannerTest {
    @Test
    fun testPlanningFromTap() {
        val planner = BehaviorPlanner()
        val intention = planner.planFromEvent(GestureEvent(GestureType.SINGLE_TAP))
        assertTrue(intention is InvestigateIntention)
    }
}
