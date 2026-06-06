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

class JourneyRepositoryCompletionStatusTest {

    private val mapper = JourneyStateMapper()

    @Test
    fun `setCompletionStatus creates missing tip state as not started`() {
        runBlocking {
            val tipId = TipId("day_01_define_real_priority")
            val dataSource = FakeJourneyDataSource(
                initialState = JourneyStateProto.getDefaultInstance()
            )
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setCompletionStatus(
                tipId = tipId,
                status = TipCompletionStatus.NOT_STARTED
            )

            val tipState = repository.observeTipState(tipId).first()

            assertEquals(tipId, tipState.tipId)
            assertEquals(TipCompletionStatus.NOT_STARTED, tipState.completionStatus)
            assertEquals(false, tipState.isBookmarked)
            assertNull(tipState.lastViewedAtEpochMillis)
            assertNull(tipState.completedAtEpochMillis)
        }
    }

    @Test
    fun `setCompletionStatus creates missing tip state as in progress`() {
        runBlocking {
            val tipId = TipId("day_01_define_real_priority")
            val dataSource = FakeJourneyDataSource(
                initialState = JourneyStateProto.getDefaultInstance()
            )
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setCompletionStatus(
                tipId = tipId,
                status = TipCompletionStatus.IN_PROGRESS
            )

            val tipState = repository.observeTipState(tipId).first()

            assertEquals(tipId, tipState.tipId)
            assertEquals(TipCompletionStatus.IN_PROGRESS, tipState.completionStatus)
            assertEquals(false, tipState.isBookmarked)
            assertNull(tipState.lastViewedAtEpochMillis)
            assertNull(tipState.completedAtEpochMillis)
        }
    }

    @Test
    fun `setCompletionStatus creates missing tip state as completed with completed timestamp`() {
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

            repository.setCompletionStatus(
                tipId = tipId,
                status = TipCompletionStatus.COMPLETED
            )

            val after = System.currentTimeMillis()
            val tipState = repository.observeTipState(tipId).first()

            assertEquals(tipId, tipState.tipId)
            assertEquals(TipCompletionStatus.COMPLETED, tipState.completionStatus)
            assertEquals(false, tipState.isBookmarked)
            assertNull(tipState.lastViewedAtEpochMillis)

            assertNotNull(tipState.completedAtEpochMillis)
            assertTrue(
                "completedAtEpochMillis should be >= before",
                tipState.completedAtEpochMillis!! >= before
            )
            assertTrue(
                "completedAtEpochMillis should be <= after",
                tipState.completedAtEpochMillis!! <= after
            )
        }
    }

    @Test
    fun `setCompletionStatus completed preserves existing completed timestamp`() {
        runBlocking {
            val tipId = TipId("day_01_define_real_priority")
            val proto = JourneyStateProto.newBuilder()
                .addTipStates(
                    TipUserStateProto.newBuilder()
                        .setTipId(tipId.value)
                        .setCompletionStatus(TipCompletionStatusProto.COMPLETED)
                        .setCompletedAtEpochMillis(456L)
                        .build()
                )
                .build()

            val dataSource = FakeJourneyDataSource(initialState = proto)
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setCompletionStatus(
                tipId = tipId,
                status = TipCompletionStatus.COMPLETED
            )

            val tipState = repository.observeTipState(tipId).first()

            assertEquals(TipCompletionStatus.COMPLETED, tipState.completionStatus)
            assertEquals(456L, tipState.completedAtEpochMillis)
        }
    }

    @Test
    fun `setCompletionStatus in progress clears completed timestamp`() {
        runBlocking {
            val tipId = TipId("day_01_define_real_priority")
            val proto = JourneyStateProto.newBuilder()
                .addTipStates(
                    TipUserStateProto.newBuilder()
                        .setTipId(tipId.value)
                        .setCompletionStatus(TipCompletionStatusProto.COMPLETED)
                        .setCompletedAtEpochMillis(456L)
                        .build()
                )
                .build()

            val dataSource = FakeJourneyDataSource(initialState = proto)
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setCompletionStatus(
                tipId = tipId,
                status = TipCompletionStatus.IN_PROGRESS
            )

            val tipState = repository.observeTipState(tipId).first()

            assertEquals(TipCompletionStatus.IN_PROGRESS, tipState.completionStatus)
            assertNull(tipState.completedAtEpochMillis)
        }
    }

    @Test
    fun `setCompletionStatus not started clears completed timestamp`() {
        runBlocking {
            val tipId = TipId("day_01_define_real_priority")
            val proto = JourneyStateProto.newBuilder()
                .addTipStates(
                    TipUserStateProto.newBuilder()
                        .setTipId(tipId.value)
                        .setCompletionStatus(TipCompletionStatusProto.COMPLETED)
                        .setCompletedAtEpochMillis(456L)
                        .build()
                )
                .build()

            val dataSource = FakeJourneyDataSource(initialState = proto)
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setCompletionStatus(
                tipId = tipId,
                status = TipCompletionStatus.NOT_STARTED
            )

            val tipState = repository.observeTipState(tipId).first()

            assertEquals(TipCompletionStatus.NOT_STARTED, tipState.completionStatus)
            assertNull(tipState.completedAtEpochMillis)
        }
    }

    @Test
    fun `setCompletionStatus preserves bookmark and viewed timestamp`() {
        runBlocking {
            val tipId = TipId("day_01_define_real_priority")
            val proto = JourneyStateProto.newBuilder()
                .addTipStates(
                    TipUserStateProto.newBuilder()
                        .setTipId(tipId.value)
                        .setCompletionStatus(TipCompletionStatusProto.IN_PROGRESS)
                        .setIsBookmarked(true)
                        .setLastViewedAtEpochMillis(123L)
                        .build()
                )
                .build()

            val dataSource = FakeJourneyDataSource(initialState = proto)
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setCompletionStatus(
                tipId = tipId,
                status = TipCompletionStatus.COMPLETED
            )

            val tipState = repository.observeTipState(tipId).first()

            assertEquals(true, tipState.isBookmarked)
            assertEquals(123L, tipState.lastViewedAtEpochMillis)
            assertEquals(TipCompletionStatus.COMPLETED, tipState.completionStatus)
            assertNotNull(tipState.completedAtEpochMillis)
        }
    }

    @Test
    fun `setCompletionStatus does not change active tip id`() {
        runBlocking {
            val activeTipId = TipId("day_01_define_real_priority")
            val completedTipId = TipId("day_02_stop_planning_by_panic")

            val proto = JourneyStateProto.newBuilder()
                .setActiveTipId(activeTipId.value)
                .build()

            val dataSource = FakeJourneyDataSource(initialState = proto)
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setCompletionStatus(
                tipId = completedTipId,
                status = TipCompletionStatus.COMPLETED
            )

            val journeyState = repository.observeJourneyState().first()

            assertEquals(activeTipId, journeyState.activeTipId)
            assertEquals(
                TipCompletionStatus.COMPLETED,
                journeyState.tipStates.getValue(completedTipId).completionStatus
            )
        }
    }

    @Test
    fun `setCompletionStatus does not remove other tip states`() {
        runBlocking {
            val firstTipId = TipId("day_01_define_real_priority")
            val secondTipId = TipId("day_02_stop_planning_by_panic")

            val proto = JourneyStateProto.newBuilder()
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

            repository.setCompletionStatus(
                tipId = secondTipId,
                status = TipCompletionStatus.IN_PROGRESS
            )

            val journeyState = repository.observeJourneyState().first()
            val firstTipState = journeyState.tipStates.getValue(firstTipId)
            val secondTipState = journeyState.tipStates.getValue(secondTipId)

            assertEquals(firstTipId, firstTipState.tipId)
            assertEquals(true, firstTipState.isBookmarked)
            assertEquals(TipCompletionStatus.COMPLETED, firstTipState.completionStatus)
            assertEquals(111L, firstTipState.completedAtEpochMillis)

            assertEquals(secondTipId, secondTipState.tipId)
            assertEquals(TipCompletionStatus.IN_PROGRESS, secondTipState.completionStatus)
            assertEquals(false, secondTipState.isBookmarked)
            assertNull(secondTipState.lastViewedAtEpochMillis)
            assertNull(secondTipState.completedAtEpochMillis)
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