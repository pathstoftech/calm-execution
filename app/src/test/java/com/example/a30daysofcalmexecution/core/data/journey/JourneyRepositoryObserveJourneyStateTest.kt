package com.example.a30daysofcalmexecution.core.data.journey

import com.example.a30daysofcalmexecution.core.data.journey.proto.JourneyStateProto
import com.example.a30daysofcalmexecution.core.data.journey.proto.TipCompletionStatusProto
import com.example.a30daysofcalmexecution.core.data.journey.proto.TipUserStateProto
import com.example.a30daysofcalmexecution.core.model.TipCompletionStatus
import com.example.a30daysofcalmexecution.core.model.TipId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class JourneyRepositoryObserveJourneyStateTest {

    private val mapper = JourneyStateMapper()

    @Test
    fun `observeJourneyState emits empty domain state when proto is default`() {
        runBlocking {
            val dataSource = FakeJourneyDataSource(
                initialState = JourneyStateProto.getDefaultInstance()
            )
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            val state = repository.observeJourneyState().first()

            assertNull(state.activeTipId)
            assertTrue(state.tipStates.isEmpty())
        }
    }

    @Test
    fun `observeJourneyState maps proto active tip id and tip states to domain`() {
        runBlocking {
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

            val dataSource = FakeJourneyDataSource(initialState = proto)
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            val state = repository.observeJourneyState().first()
            val tipId = TipId("day_01_define_real_priority")
            val tipState = state.tipStates.getValue(tipId)

            assertEquals(tipId, state.activeTipId)
            assertEquals(tipId, tipState.tipId)
            assertEquals(TipCompletionStatus.IN_PROGRESS, tipState.completionStatus)
            assertEquals(true, tipState.isBookmarked)
            assertEquals(123L, tipState.lastViewedAtEpochMillis)
            assertNull(tipState.completedAtEpochMillis)
        }
    }

    @Test
    fun `observeJourneyState emits updated domain state after data source update`() {
        runBlocking {
            val dataSource = FakeJourneyDataSource(
                initialState = JourneyStateProto.getDefaultInstance()
            )
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            dataSource.updateJourneyState { current ->
                current.toBuilder()
                    .setActiveTipId("day_02_stop_planning_by_panic")
                    .addTipStates(
                        TipUserStateProto.newBuilder()
                            .setTipId("day_02_stop_planning_by_panic")
                            .setCompletionStatus(TipCompletionStatusProto.COMPLETED)
                            .setIsBookmarked(false)
                            .setLastViewedAtEpochMillis(100L)
                            .setCompletedAtEpochMillis(200L)
                            .build()
                    )
                    .build()
            }

            val state = repository.observeJourneyState().first()
            val tipId = TipId("day_02_stop_planning_by_panic")
            val tipState = state.tipStates.getValue(tipId)

            assertEquals(tipId, state.activeTipId)
            assertEquals(TipCompletionStatus.COMPLETED, tipState.completionStatus)
            assertEquals(100L, tipState.lastViewedAtEpochMillis)
            assertEquals(200L, tipState.completedAtEpochMillis)
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