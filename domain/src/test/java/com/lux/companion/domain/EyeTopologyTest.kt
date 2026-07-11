package com.lux.companion.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class EyeTopologyTest {

    @Test
    fun testLerp() {
        val start = EyeTopology(widthScale = 1.0f)
        val end = EyeTopology(widthScale = 2.0f)

        val mid = start.lerp(end, 0.5f)

        assertEquals(1.5f, mid.widthScale, 0.01f)
    }
}
