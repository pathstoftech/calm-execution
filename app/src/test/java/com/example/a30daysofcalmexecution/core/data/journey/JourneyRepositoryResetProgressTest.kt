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

class JourneyRepositoryResetProgressTest {

    private val mapper = JourneyStateMapper()

    @Test
    fun `resetProgress clears active tip id and all tip states`() {
        runBlocking {
            val firstTipId = TipId("day_01_define_real_priority")
            val secondTipId = TipId("day_02_stop_planning_by_panic")

            val proto = JourneyStateProto.newBuilder()
                .setActiveTipId(secondTipId.value)
                .addTipStates(
                    TipUserStateProto.newBuilder()
                        .setTipId(firstTipId.value)
                        .setCompletionStatus(TipCompletionStatusProto.COMPLETED)
                        .setIsBookmarked(true)
                        .setLastViewedAtEpochMillis(100L)
                        .setCompletedAtEpochMillis(200L)
                        .build()
                )
                .addTipStates(
                    TipUserStateProto.newBuilder()
                        .setTipId(secondTipId.value)
                        .setCompletionStatus(TipCompletionStatusProto.IN_PROGRESS)
                        .setIsBookmarked(false)
                        .setLastViewedAtEpochMillis(300L)
                        .build()
                )
                .build()

            val dataSource = FakeJourneyDataSource(initialState = proto)
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.resetProgress()

            val journeyState = repository.observeJourneyState().first()

            assertNull(journeyState.activeTipId)
            assertTrue(journeyState.tipStates.isEmpty())
        }
    }

    @Test
    fun `resetProgress makes previously stored tip emit default tip state`() {
        runBlocking {
            val tipId = TipId("day_01_define_real_priority")

            val proto = JourneyStateProto.newBuilder()
                .setActiveTipId(tipId.value)
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

            val dataSource = FakeJourneyDataSource(initialState = proto)
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.resetProgress()

            val tipState = repository.observeTipState(tipId).first()

            assertEquals(tipId, tipState.tipId)
            assertEquals(false, tipState.isBookmarked)
            assertEquals(TipCompletionStatus.NOT_STARTED, tipState.completionStatus)
            assertNull(tipState.lastViewedAtEpochMillis)
            assertNull(tipState.completedAtEpochMillis)
        }
    }

    @Test
    fun `resetProgress is safe when journey state is already empty`() {
        runBlocking {
            val dataSource = FakeJourneyDataSource(
                initialState = JourneyStateProto.getDefaultInstance()
            )
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.resetProgress()

            val journeyState = repository.observeJourneyState().first()

            assertNull(journeyState.activeTipId)
            assertTrue(journeyState.tipStates.isEmpty())
        }
    }

    @Test
    fun `resetProgress writes empty journey state back to data source`() {
        runBlocking {
            val tipId = TipId("day_01_define_real_priority")

            val proto = JourneyStateProto.newBuilder()
                .setActiveTipId(tipId.value)
                .addTipStates(
                    TipUserStateProto.newBuilder()
                        .setTipId(tipId.value)
                        .setCompletionStatus(TipCompletionStatusProto.COMPLETED)
                        .setIsBookmarked(true)
                        .setCompletedAtEpochMillis(200L)
                        .build()
                )
                .build()

            val dataSource = FakeJourneyDataSource(initialState = proto)
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.resetProgress()

            val storedProto = dataSource.journeyState.first()

            assertEquals("", storedProto.activeTipId)
            assertTrue(storedProto.tipStatesList.isEmpty())
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