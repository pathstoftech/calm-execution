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
import org.junit.Assert.assertNull
import org.junit.Test

class JourneyRepositoryObserveTipStateTest {

    private val mapper = JourneyStateMapper()

    @Test
    fun `observeTipState emits default tip state when tip is missing`() {
        runBlocking {
            val tipId = TipId("day_01_define_real_priority")
            val dataSource = FakeJourneyDataSource(
                initialState = JourneyStateProto.getDefaultInstance()
            )
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            val tipState = repository.observeTipState(tipId).first()

            assertEquals(tipId, tipState.tipId)
            assertEquals(false, tipState.isBookmarked)
            assertEquals(TipCompletionStatus.NOT_STARTED, tipState.completionStatus)
            assertNull(tipState.lastViewedAtEpochMillis)
            assertNull(tipState.completedAtEpochMillis)
        }
    }

    @Test
    fun `observeTipState emits requested tip state when it exists`() {
        runBlocking {
            val requestedTipId = TipId("day_01_define_real_priority")
            val proto = JourneyStateProto.newBuilder()
                .addTipStates(
                    TipUserStateProto.newBuilder()
                        .setTipId(requestedTipId.value)
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

            val tipState = repository.observeTipState(requestedTipId).first()

            assertEquals(requestedTipId, tipState.tipId)
            assertEquals(true, tipState.isBookmarked)
            assertEquals(TipCompletionStatus.IN_PROGRESS, tipState.completionStatus)
            assertEquals(123L, tipState.lastViewedAtEpochMillis)
            assertNull(tipState.completedAtEpochMillis)
        }
    }

    @Test
    fun `observeTipState returns only requested tip state`() {
        runBlocking {
            val requestedTipId = TipId("day_02_stop_planning_by_panic")
            val otherTipId = TipId("day_01_define_real_priority")

            val proto = JourneyStateProto.newBuilder()
                .addTipStates(
                    TipUserStateProto.newBuilder()
                        .setTipId(otherTipId.value)
                        .setCompletionStatus(TipCompletionStatusProto.COMPLETED)
                        .setIsBookmarked(true)
                        .setCompletedAtEpochMillis(999L)
                        .build()
                )
                .addTipStates(
                    TipUserStateProto.newBuilder()
                        .setTipId(requestedTipId.value)
                        .setCompletionStatus(TipCompletionStatusProto.IN_PROGRESS)
                        .setIsBookmarked(false)
                        .setLastViewedAtEpochMillis(222L)
                        .build()
                )
                .build()

            val dataSource = FakeJourneyDataSource(initialState = proto)
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            val tipState = repository.observeTipState(requestedTipId).first()

            assertEquals(requestedTipId, tipState.tipId)
            assertEquals(false, tipState.isBookmarked)
            assertEquals(TipCompletionStatus.IN_PROGRESS, tipState.completionStatus)
            assertEquals(222L, tipState.lastViewedAtEpochMillis)
            assertNull(tipState.completedAtEpochMillis)
        }
    }

    @Test
    fun `observeTipState emits default state when only other tips exist`() {
        runBlocking {
            val requestedTipId = TipId("day_02_stop_planning_by_panic")
            val otherTipId = TipId("day_01_define_real_priority")

            val proto = JourneyStateProto.newBuilder()
                .addTipStates(
                    TipUserStateProto.newBuilder()
                        .setTipId(otherTipId.value)
                        .setCompletionStatus(TipCompletionStatusProto.COMPLETED)
                        .setIsBookmarked(true)
                        .setCompletedAtEpochMillis(999L)
                        .build()
                )
                .build()

            val dataSource = FakeJourneyDataSource(initialState = proto)
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            val tipState = repository.observeTipState(requestedTipId).first()

            assertEquals(requestedTipId, tipState.tipId)
            assertEquals(false, tipState.isBookmarked)
            assertEquals(TipCompletionStatus.NOT_STARTED, tipState.completionStatus)
            assertNull(tipState.lastViewedAtEpochMillis)
            assertNull(tipState.completedAtEpochMillis)
        }
    }

    @Test
    fun `observeTipState reflects data source updates for requested tip`() {
        runBlocking {
            val tipId = TipId("day_01_define_real_priority")
            val dataSource = FakeJourneyDataSource(
                initialState = JourneyStateProto.getDefaultInstance()
            )
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            val initial = repository.observeTipState(tipId).first()

            dataSource.updateJourneyState { current ->
                current.toBuilder()
                    .addTipStates(
                        TipUserStateProto.newBuilder()
                            .setTipId(tipId.value)
                            .setCompletionStatus(TipCompletionStatusProto.COMPLETED)
                            .setIsBookmarked(true)
                            .setLastViewedAtEpochMillis(100L)
                            .setCompletedAtEpochMillis(200L)
                            .build()
                    )
                    .build()
            }

            val updated = repository.observeTipState(tipId).first()

            assertEquals(TipCompletionStatus.NOT_STARTED, initial.completionStatus)
            assertEquals(false, initial.isBookmarked)

            assertEquals(tipId, updated.tipId)
            assertEquals(TipCompletionStatus.COMPLETED, updated.completionStatus)
            assertEquals(true, updated.isBookmarked)
            assertEquals(100L, updated.lastViewedAtEpochMillis)
            assertEquals(200L, updated.completedAtEpochMillis)
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