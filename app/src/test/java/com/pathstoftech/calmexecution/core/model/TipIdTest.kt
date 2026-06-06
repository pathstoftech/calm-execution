package com.pathstoftech.calmexecution.core.model

import org.junit.Assert.assertEquals
import org.junit.Test

class TipIdTest {

    @Test
    fun `stores non-blank value`() {
        val id = TipId("day_01_define_real_priority")
        assertEquals("day_01_define_real_priority", id.value)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `rejects blank value`() {
        TipId(" ")
    }
}