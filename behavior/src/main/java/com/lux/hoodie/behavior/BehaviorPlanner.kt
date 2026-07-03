package com.lux.hoodie.behavior

import com.lux.hoodie.domain.LuxEvent
import com.lux.hoodie.domain.GestureEvent
import com.lux.hoodie.domain.GestureType
import com.lux.hoodie.domain.Intention

/**
 * Decides which intention to pursue based on incoming events.
 */
class BehaviorPlanner {
    fun planFromEvent(event: LuxEvent): Intention? {
        return when (event) {
            is GestureEvent -> {
                when (event.type) {
                    GestureType.SINGLE_TAP -> InvestigateIntention()
                    GestureType.DEVICE_PICKUP -> WakeUpIntention()
                    GestureType.LONG_PRESS -> IdleObserveIntention()
                    GestureType.DOUBLE_TAP -> CuriosityTiltIntention()
                    GestureType.SWIPE_DOWN -> DrowseIntention()
                    else -> null
                }
            }
            else -> null
        }
    }
}
