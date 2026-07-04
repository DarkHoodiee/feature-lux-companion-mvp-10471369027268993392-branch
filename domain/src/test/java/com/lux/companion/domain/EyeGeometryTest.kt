package com.lux.companion.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class EyeGeometryTest {
    @Test
    fun testLerp() {
        val start = EyeGeometry(width = 100f)
        val stop = EyeGeometry(width = 200f)
        val result = start.lerp(stop, 0.5f)
        assertEquals(150f, result.width, 0.01f)
    }
}
