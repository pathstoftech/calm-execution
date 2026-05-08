package com.example.a30daysofcalmexecution.core.designsystem.theme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MotionTokensTest {

    @Test
    fun `default motion tokens use increasing non-negative durations`() {
        val tokens = DefaultCalmMotionTokens

        assertEquals(0, tokens.instantDurationMillis)
        assertTrue(tokens.shortDurationMillis > tokens.instantDurationMillis)
        assertTrue(tokens.mediumDurationMillis > tokens.shortDurationMillis)
        assertTrue(tokens.longDurationMillis > tokens.mediumDurationMillis)
    }

    @Test
    fun `reduced motion tokens use zero durations`() {
        val tokens = ReducedCalmMotionTokens

        assertEquals(0, tokens.instantDurationMillis)
        assertEquals(0, tokens.shortDurationMillis)
        assertEquals(0, tokens.mediumDurationMillis)
        assertEquals(0, tokens.longDurationMillis)
    }
}