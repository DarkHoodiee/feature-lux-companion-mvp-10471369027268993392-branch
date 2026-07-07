package com.lux.hoodie.events

import com.lux.hoodie.domain.LuxEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class EventBusTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testEventPublishing() = runTest {
        val testEvent = object : LuxEvent {
            override val timestamp = 123L
        }

        launch {
            EventBus.publish(testEvent)
        }

        val received = EventBus.events.first()
        assertEquals(123L, received.timestamp)
    }
}
