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

class JourneyRepositoryBookmarkTest {

    private val mapper = JourneyStateMapper()

    @Test
    fun `setBookmarked true creates missing tip state as bookmarked`() {
        runBlocking {
            val tipId = TipId("day_01_define_real_priority")
            val dataSource = FakeJourneyDataSource(
                initialState = JourneyStateProto.getDefaultInstance()
            )
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setBookmarked(
                tipId = tipId,
                bookmarked = true
            )

            val tipState = repository.observeTipState(tipId).first()

            assertEquals(tipId, tipState.tipId)
            assertEquals(true, tipState.isBookmarked)
            assertEquals(TipCompletionStatus.NOT_STARTED, tipState.completionStatus)
            assertNull(tipState.lastViewedAtEpochMillis)
            assertNull(tipState.completedAtEpochMillis)
        }
    }

    @Test
    fun `setBookmarked false creates missing tip state as not bookmarked`() {
        runBlocking {
            val tipId = TipId("day_01_define_real_priority")
            val dataSource = FakeJourneyDataSource(
                initialState = JourneyStateProto.getDefaultInstance()
            )
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setBookmarked(
                tipId = tipId,
                bookmarked = false
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
    fun `setBookmarked true updates existing unbookmarked tip state`() {
        runBlocking {
            val tipId = TipId("day_01_define_real_priority")
            val proto = JourneyStateProto.newBuilder()
                .addTipStates(
                    TipUserStateProto.newBuilder()
                        .setTipId(tipId.value)
                        .setCompletionStatus(TipCompletionStatusProto.IN_PROGRESS)
                        .setIsBookmarked(false)
                        .setLastViewedAtEpochMillis(123L)
                        .build()
                )
                .build()

            val dataSource = FakeJourneyDataSource(initialState = proto)
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setBookmarked(
                tipId = tipId,
                bookmarked = true
            )

            val tipState = repository.observeTipState(tipId).first()

            assertEquals(tipId, tipState.tipId)
            assertEquals(true, tipState.isBookmarked)
            assertEquals(TipCompletionStatus.IN_PROGRESS, tipState.completionStatus)
            assertEquals(123L, tipState.lastViewedAtEpochMillis)
            assertNull(tipState.completedAtEpochMillis)
        }
    }

    @Test
    fun `setBookmarked false updates existing bookmarked tip state`() {
        runBlocking {
            val tipId = TipId("day_01_define_real_priority")
            val proto = JourneyStateProto.newBuilder()
                .addTipStates(
                    TipUserStateProto.newBuilder()
                        .setTipId(tipId.value)
                        .setCompletionStatus(TipCompletionStatusProto.COMPLETED)
                        .setIsBookmarked(true)
                        .setLastViewedAtEpochMillis(123L)
                        .setCompletedAtEpochMillis(456L)
                        .build()
                )
                .build()

            val dataSource = FakeJourneyDataSource(initialState = proto)
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setBookmarked(
                tipId = tipId,
                bookmarked = false
            )

            val tipState = repository.observeTipState(tipId).first()

            assertEquals(tipId, tipState.tipId)
            assertEquals(false, tipState.isBookmarked)
            assertEquals(TipCompletionStatus.COMPLETED, tipState.completionStatus)
            assertEquals(123L, tipState.lastViewedAtEpochMillis)
            assertEquals(456L, tipState.completedAtEpochMillis)
        }
    }

    @Test
    fun `setBookmarked does not change active tip id`() {
        runBlocking {
            val activeTipId = TipId("day_01_define_real_priority")
            val bookmarkedTipId = TipId("day_02_stop_planning_by_panic")

            val proto = JourneyStateProto.newBuilder()
                .setActiveTipId(activeTipId.value)
                .build()

            val dataSource = FakeJourneyDataSource(initialState = proto)
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setBookmarked(
                tipId = bookmarkedTipId,
                bookmarked = true
            )

            val journeyState = repository.observeJourneyState().first()

            assertEquals(activeTipId, journeyState.activeTipId)
            assertEquals(true, journeyState.tipStates.getValue(bookmarkedTipId).isBookmarked)
        }
    }

    @Test
    fun `setBookmarked does not remove other tip states`() {
        runBlocking {
            val firstTipId = TipId("day_01_define_real_priority")
            val secondTipId = TipId("day_02_stop_planning_by_panic")

            val proto = JourneyStateProto.newBuilder()
                .addTipStates(
                    TipUserStateProto.newBuilder()
                        .setTipId(firstTipId.value)
                        .setCompletionStatus(TipCompletionStatusProto.COMPLETED)
                        .setIsBookmarked(false)
                        .setCompletedAtEpochMillis(111L)
                        .build()
                )
                .build()

            val dataSource = FakeJourneyDataSource(initialState = proto)
            val repository = JourneyRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setBookmarked(
                tipId = secondTipId,
                bookmarked = true
            )

            val journeyState = repository.observeJourneyState().first()
            val firstTipState = journeyState.tipStates.getValue(firstTipId)
            val secondTipState = journeyState.tipStates.getValue(secondTipId)

            assertEquals(firstTipId, firstTipState.tipId)
            assertEquals(false, firstTipState.isBookmarked)
            assertEquals(TipCompletionStatus.COMPLETED, firstTipState.completionStatus)
            assertEquals(111L, firstTipState.completedAtEpochMillis)

            assertEquals(secondTipId, secondTipState.tipId)
            assertEquals(true, secondTipState.isBookmarked)
            assertEquals(TipCompletionStatus.NOT_STARTED, secondTipState.completionStatus)
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