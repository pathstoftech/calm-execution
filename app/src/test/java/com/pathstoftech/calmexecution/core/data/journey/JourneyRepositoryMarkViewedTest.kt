package com.pathstoftech.calmexecution.core.data.journey

import com.pathstoftech.calmexecution.core.data.journey.proto.JourneyStateProto
import com.pathstoftech.calmexecution.core.data.journey.proto.TipCompletionStatusProto
import com.pathstoftech.calmexecution.core.data.journey.proto.TipUserStateProto
import com.pathstoftech.calmexecution.core.model.TipCompletionStatus
import com.pathstoftech.calmexecution.core.model.TipId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class JourneyRepositoryMarkViewedTest {

    private val mapper = JourneyStateMapper()

    @Test
    fun `markViewed creates missing tip state with viewed timestamp`() {
        runBlocking {
            val tipId = TipId("day_01_define_real_priority")
            val dataSource = FakeJourneyDataSource(
                initialState = JourneyStateProto.getDefaultInstance()
            )
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            val before = System.currentTimeMillis()

            repository.markViewed(tipId)

            val after = System.currentTimeMillis()
            val tipState = repository.observeTipState(tipId).first()

            assertEquals(tipId, tipState.tipId)
            assertEquals(false, tipState.isBookmarked)
            assertEquals(TipCompletionStatus.NOT_STARTED, tipState.completionStatus)
            assertNull(tipState.completedAtEpochMillis)

            assertNotNull(tipState.lastViewedAtEpochMillis)
            assertTrue(
                "lastViewedAtEpochMillis should be >= before",
                tipState.lastViewedAtEpochMillis!! >= before
            )
            assertTrue(
                "lastViewedAtEpochMillis should be <= after",
                tipState.lastViewedAtEpochMillis!! <= after
            )
        }
    }

    @Test
    fun `markViewed sets active tip id`() {
        runBlocking {
            val tipId = TipId("day_02_stop_planning_by_panic")
            val dataSource = FakeJourneyDataSource(
                initialState = JourneyStateProto.getDefaultInstance()
            )
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.markViewed(tipId)

            val journeyState = repository.observeJourneyState().first()

            assertEquals(tipId, journeyState.activeTipId)
        }
    }

    @Test
    fun `markViewed updates existing tip state without changing bookmark or completion`() {
        runBlocking {
            val tipId = TipId("day_01_define_real_priority")
            val proto = JourneyStateProto.newBuilder()
                .addTipStates(
                    TipUserStateProto.newBuilder()
                        .setTipId(tipId.value)
                        .setCompletionStatus(TipCompletionStatusProto.COMPLETED)
                        .setIsBookmarked(true)
                        .setCompletedAtEpochMillis(500L)
                        .build()
                )
                .build()

            val dataSource = FakeJourneyDataSource(initialState = proto)
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            val before = System.currentTimeMillis()

            repository.markViewed(tipId)

            val after = System.currentTimeMillis()
            val tipState = repository.observeTipState(tipId).first()

            assertEquals(tipId, tipState.tipId)
            assertEquals(true, tipState.isBookmarked)
            assertEquals(TipCompletionStatus.COMPLETED, tipState.completionStatus)
            assertEquals(500L, tipState.completedAtEpochMillis)

            assertNotNull(tipState.lastViewedAtEpochMillis)
            assertTrue(tipState.lastViewedAtEpochMillis!! >= before)
            assertTrue(tipState.lastViewedAtEpochMillis!! <= after)
        }
    }

    @Test
    fun `markViewed changes active tip without removing other tip states`() {
        runBlocking {
            val firstTipId = TipId("day_01_define_real_priority")
            val secondTipId = TipId("day_02_stop_planning_by_panic")

            val proto = JourneyStateProto.newBuilder()
                .setActiveTipId(firstTipId.value)
                .addTipStates(
                    TipUserStateProto.newBuilder()
                        .setTipId(firstTipId.value)
                        .setCompletionStatus(TipCompletionStatusProto.COMPLETED)
                        .setIsBookmarked(true)
                        .setCompletedAtEpochMillis(111L)
                        .build()
                )
                .build()

            val dataSource = FakeJourneyDataSource(initialState = proto)
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.markViewed(secondTipId)

            val journeyState = repository.observeJourneyState().first()
            val firstTipState = journeyState.tipStates.getValue(firstTipId)
            val secondTipState = journeyState.tipStates.getValue(secondTipId)

            assertEquals(secondTipId, journeyState.activeTipId)

            assertEquals(firstTipId, firstTipState.tipId)
            assertEquals(true, firstTipState.isBookmarked)
            assertEquals(TipCompletionStatus.COMPLETED, firstTipState.completionStatus)
            assertEquals(111L, firstTipState.completedAtEpochMillis)

            assertEquals(secondTipId, secondTipState.tipId)
            assertEquals(TipCompletionStatus.NOT_STARTED, secondTipState.completionStatus)
            assertNotNull(secondTipState.lastViewedAtEpochMillis)
        }
    }

    private class FakeJourneyDataSource(
        initialState: JourneyStateProto
    ) : JourneyDataSource {

        private val state = MutableStateFlow(initialState)

        override val journeyState: Flow<JourneyStateProto> =
            state.asStateFlow()

        override suspend fun updateJourneyState(
            transform: suspend (JourneyStateProto) -> JourneyStateProto
        ): JourneyStateProto {
            val updated = transform(state.value)
            state.value = updated
            return updated
        }
    }
}