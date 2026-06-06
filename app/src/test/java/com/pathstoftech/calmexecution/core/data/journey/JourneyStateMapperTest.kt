package com.pathstoftech.calmexecution.core.data.journey

import com.pathstoftech.calmexecution.core.data.journey.proto.JourneyStateProto
import com.pathstoftech.calmexecution.core.data.journey.proto.TipCompletionStatusProto
import com.pathstoftech.calmexecution.core.data.journey.proto.TipUserStateProto
import com.pathstoftech.calmexecution.core.model.JourneyUserState
import com.pathstoftech.calmexecution.core.model.TipCompletionStatus
import com.pathstoftech.calmexecution.core.model.TipId
import com.pathstoftech.calmexecution.core.model.TipUserState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class JourneyStateMapperTest {

    private val mapper = JourneyStateMapper()

    @Test
    fun `toDomain maps empty proto to empty domain state`() {
        val domain = mapper.toDomain(JourneyStateProto.getDefaultInstance())

        assertNull(domain.activeTipId)
        assertTrue(domain.tipStates.isEmpty())
    }

    @Test
    fun `toDomain maps active tip id and tip states`() {
        val proto = JourneyStateProto.newBuilder()
            .setActiveTipId("day_01_define_real_priority")
            .addTipStates(
                TipUserStateProto.newBuilder()
                    .setTipId("day_01_define_real_priority")
                    .setCompletionStatus(TipCompletionStatusProto.IN_PROGRESS)
                    .setIsBookmarked(true)
                    .setLastViewedAtEpochMillis(123L)
                    .build()
            )
            .build()

        val domain = mapper.toDomain(proto)

        val tipId = TipId("day_01_define_real_priority")
        val tipState = domain.tipStates.getValue(tipId)

        assertEquals(tipId, domain.activeTipId)
        assertEquals(tipId, tipState.tipId)
        assertEquals(TipCompletionStatus.IN_PROGRESS, tipState.completionStatus)
        assertEquals(true, tipState.isBookmarked)
        assertEquals(123L, tipState.lastViewedAtEpochMillis)
        assertNull(tipState.completedAtEpochMillis)
    }

    @Test
    fun `toDomain maps zero timestamps to null`() {
        val proto = JourneyStateProto.newBuilder()
            .addTipStates(
                TipUserStateProto.newBuilder()
                    .setTipId("day_01_define_real_priority")
                    .setCompletionStatus(TipCompletionStatusProto.NOT_STARTED)
                    .setLastViewedAtEpochMillis(0L)
                    .setCompletedAtEpochMillis(0L)
                    .build()
            )
            .build()

        val domain = mapper.toDomain(proto)
        val tipState = domain.tipStates.getValue(TipId("day_01_define_real_priority"))

        assertNull(tipState.lastViewedAtEpochMillis)
        assertNull(tipState.completedAtEpochMillis)
    }

    @Test
    fun `toDomain keeps completed timestamp only for completed status`() {
        val proto = JourneyStateProto.newBuilder()
            .addTipStates(
                TipUserStateProto.newBuilder()
                    .setTipId("day_01_define_real_priority")
                    .setCompletionStatus(TipCompletionStatusProto.IN_PROGRESS)
                    .setCompletedAtEpochMillis(456L)
                    .build()
            )
            .build()

        val domain = mapper.toDomain(proto)
        val tipState = domain.tipStates.getValue(TipId("day_01_define_real_priority"))

        assertEquals(TipCompletionStatus.IN_PROGRESS, tipState.completionStatus)
        assertNull(tipState.completedAtEpochMillis)
    }

    @Test
    fun `toDomain maps completed status with completed timestamp`() {
        val proto = JourneyStateProto.newBuilder()
            .addTipStates(
                TipUserStateProto.newBuilder()
                    .setTipId("day_01_define_real_priority")
                    .setCompletionStatus(TipCompletionStatusProto.COMPLETED)
                    .setCompletedAtEpochMillis(456L)
                    .build()
            )
            .build()

        val domain = mapper.toDomain(proto)
        val tipState = domain.tipStates.getValue(TipId("day_01_define_real_priority"))

        assertEquals(TipCompletionStatus.COMPLETED, tipState.completionStatus)
        assertEquals(456L, tipState.completedAtEpochMillis)
    }

    @Test
    fun `toDomain skips proto tip states with blank tip id`() {
        val proto = JourneyStateProto.newBuilder()
            .addTipStates(
                TipUserStateProto.newBuilder()
                    .setTipId("")
                    .setCompletionStatus(TipCompletionStatusProto.COMPLETED)
                    .build()
            )
            .build()

        val domain = mapper.toDomain(proto)

        assertTrue(domain.tipStates.isEmpty())
    }

    @Test
    fun `toDomain maps unspecified completion status to not started`() {
        val proto = JourneyStateProto.newBuilder()
            .addTipStates(
                TipUserStateProto.newBuilder()
                    .setTipId("day_01_define_real_priority")
                    .setCompletionStatus(TipCompletionStatusProto.TIP_COMPLETION_STATUS_UNSPECIFIED)
                    .build()
            )
            .build()

        val domain = mapper.toDomain(proto)
        val tipState = domain.tipStates.getValue(TipId("day_01_define_real_priority"))

        assertEquals(TipCompletionStatus.NOT_STARTED, tipState.completionStatus)
    }

    @Test
    fun `toProto maps domain state to proto`() {
        val tipId = TipId("day_01_define_real_priority")
        val domain = JourneyUserState(
            activeTipId = tipId,
            tipStates = mapOf(
                tipId to TipUserState(
                    tipId = tipId,
                    isBookmarked = true,
                    completionStatus = TipCompletionStatus.COMPLETED,
                    lastViewedAtEpochMillis = 123L,
                    completedAtEpochMillis = 456L
                )
            )
        )

        val proto = mapper.toProto(domain)
        val tipStateProto = proto.tipStatesList.first()

        assertEquals("day_01_define_real_priority", proto.activeTipId)
        assertEquals("day_01_define_real_priority", tipStateProto.tipId)
        assertEquals(TipCompletionStatusProto.COMPLETED, tipStateProto.completionStatus)
        assertEquals(true, tipStateProto.isBookmarked)
        assertEquals(123L, tipStateProto.lastViewedAtEpochMillis)
        assertEquals(456L, tipStateProto.completedAtEpochMillis)
    }

    @Test
    fun `toProto maps null timestamps to zero`() {
        val tipId = TipId("day_01_define_real_priority")
        val domain = JourneyUserState(
            tipStates = mapOf(
                tipId to TipUserState(
                    tipId = tipId,
                    completionStatus = TipCompletionStatus.NOT_STARTED,
                    lastViewedAtEpochMillis = null,
                    completedAtEpochMillis = null
                )
            )
        )

        val proto = mapper.toProto(domain)
        val tipStateProto = proto.tipStatesList.first()

        assertEquals(0L, tipStateProto.lastViewedAtEpochMillis)
        assertEquals(0L, tipStateProto.completedAtEpochMillis)
    }

    @Test
    fun `round trip preserves valid journey user state`() {
        val tipId = TipId("day_01_define_real_priority")
        val original = JourneyUserState(
            activeTipId = tipId,
            tipStates = mapOf(
                tipId to TipUserState(
                    tipId = tipId,
                    isBookmarked = true,
                    completionStatus = TipCompletionStatus.COMPLETED,
                    lastViewedAtEpochMillis = 123L,
                    completedAtEpochMillis = 456L
                )
            )
        )

        val restored = mapper.toDomain(mapper.toProto(original))

        assertEquals(original, restored)
    }
}