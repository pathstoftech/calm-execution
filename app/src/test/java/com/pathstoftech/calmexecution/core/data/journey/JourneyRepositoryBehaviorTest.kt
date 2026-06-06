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

class JourneyRepositoryBehaviorTest {

    private val mapper = JourneyStateMapper()

    @Test
    fun `observeJourneyState exposes default domain state from empty proto`() {
        runBlocking {
            val repository = createRepository(
                dataSource = FakeJourneyDataSource(
                    initialState = JourneyStateProto.getDefaultInstance()
                )
            )

            val state = repository.observeJourneyState().first()

            assertNull(state.activeTipId)
            assertTrue(state.tipStates.isEmpty())
        }
    }

    @Test
    fun `repository supports realistic tip progress lifecycle`() {
        runBlocking {
            val tipId = TipId("day_01_define_real_priority")
            val dataSource = FakeJourneyDataSource(
                initialState = JourneyStateProto.getDefaultInstance()
            )
            val repository = createRepository(dataSource)

            val beforeViewed = System.currentTimeMillis()

            repository.markViewed(tipId)

            val afterViewed = System.currentTimeMillis()
            val viewedState = repository.observeTipState(tipId).first()
            val viewedAt = viewedState.lastViewedAtEpochMillis

            assertEquals(tipId, repository.observeJourneyState().first().activeTipId)
            assertEquals(tipId, viewedState.tipId)
            assertNotNull(viewedAt)
            assertTrue(viewedAt!! >= beforeViewed)
            assertTrue(viewedAt <= afterViewed)
            assertEquals(false, viewedState.isBookmarked)
            assertEquals(TipCompletionStatus.NOT_STARTED, viewedState.completionStatus)
            assertNull(viewedState.completedAtEpochMillis)

            repository.setBookmarked(
                tipId = tipId,
                bookmarked = true
            )

            repository.setCompletionStatus(
                tipId = tipId,
                status = TipCompletionStatus.IN_PROGRESS
            )

            val beforeCompleted = System.currentTimeMillis()

            repository.setCompletionStatus(
                tipId = tipId,
                status = TipCompletionStatus.COMPLETED
            )

            val afterCompleted = System.currentTimeMillis()
            val completedState = repository.observeTipState(tipId).first()
            val completedAt = completedState.completedAtEpochMillis

            assertEquals(tipId, completedState.tipId)
            assertEquals(true, completedState.isBookmarked)
            assertEquals(TipCompletionStatus.COMPLETED, completedState.completionStatus)
            assertEquals(viewedAt, completedState.lastViewedAtEpochMillis)

            assertNotNull(completedAt)
            assertTrue(completedAt!! >= beforeCompleted)
            assertTrue(completedAt <= afterCompleted)

            val storedProto = dataSource.journeyState.first()
            val storedTipState = storedProto.tipStatesList.first()

            assertEquals(tipId.value, storedProto.activeTipId)
            assertEquals(tipId.value, storedTipState.tipId)
            assertEquals(true, storedTipState.isBookmarked)
            assertEquals(TipCompletionStatusProto.COMPLETED, storedTipState.completionStatus)
            assertEquals(viewedAt, storedTipState.lastViewedAtEpochMillis)
            assertEquals(completedAt, storedTipState.completedAtEpochMillis)
        }
    }

    @Test
    fun `completion timestamp is cleared when completed tip moves back to in progress`() {
        runBlocking {
            val tipId = TipId("day_01_define_real_priority")
            val initialState = JourneyStateProto.newBuilder()
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

            val repository = createRepository(
                dataSource = FakeJourneyDataSource(initialState)
            )

            repository.setCompletionStatus(
                tipId = tipId,
                status = TipCompletionStatus.IN_PROGRESS
            )

            val tipState = repository.observeTipState(tipId).first()

            assertEquals(tipId, tipState.tipId)
            assertEquals(true, tipState.isBookmarked)
            assertEquals(100L, tipState.lastViewedAtEpochMillis)
            assertEquals(TipCompletionStatus.IN_PROGRESS, tipState.completionStatus)
            assertNull(tipState.completedAtEpochMillis)
        }
    }

    @Test
    fun `mutating one tip preserves another tip state`() {
        runBlocking {
            val firstTipId = TipId("day_01_define_real_priority")
            val secondTipId = TipId("day_02_stop_planning_by_panic")

            val initialState = JourneyStateProto.newBuilder()
                .addTipStates(
                    TipUserStateProto.newBuilder()
                        .setTipId(firstTipId.value)
                        .setCompletionStatus(TipCompletionStatusProto.COMPLETED)
                        .setIsBookmarked(true)
                        .setLastViewedAtEpochMillis(100L)
                        .setCompletedAtEpochMillis(200L)
                        .build()
                )
                .build()

            val repository = createRepository(
                dataSource = FakeJourneyDataSource(initialState)
            )

            repository.setBookmarked(
                tipId = secondTipId,
                bookmarked = true
            )

            val journeyState = repository.observeJourneyState().first()
            val firstTipState = journeyState.tipStates.getValue(firstTipId)
            val secondTipState = journeyState.tipStates.getValue(secondTipId)

            assertEquals(firstTipId, firstTipState.tipId)
            assertEquals(true, firstTipState.isBookmarked)
            assertEquals(TipCompletionStatus.COMPLETED, firstTipState.completionStatus)
            assertEquals(100L, firstTipState.lastViewedAtEpochMillis)
            assertEquals(200L, firstTipState.completedAtEpochMillis)

            assertEquals(secondTipId, secondTipState.tipId)
            assertEquals(true, secondTipState.isBookmarked)
            assertEquals(TipCompletionStatus.NOT_STARTED, secondTipState.completionStatus)
            assertNull(secondTipState.lastViewedAtEpochMillis)
            assertNull(secondTipState.completedAtEpochMillis)
        }
    }

    @Test
    fun `resetProgress clears accumulated journey state`() {
        runBlocking {
            val firstTipId = TipId("day_01_define_real_priority")
            val secondTipId = TipId("day_02_stop_planning_by_panic")
            val dataSource = FakeJourneyDataSource(
                initialState = JourneyStateProto.getDefaultInstance()
            )
            val repository = createRepository(dataSource)

            repository.markViewed(firstTipId)
            repository.setBookmarked(
                tipId = firstTipId,
                bookmarked = true
            )
            repository.setCompletionStatus(
                tipId = firstTipId,
                status = TipCompletionStatus.COMPLETED
            )
            repository.markViewed(secondTipId)
            repository.setCompletionStatus(
                tipId = secondTipId,
                status = TipCompletionStatus.IN_PROGRESS
            )

            repository.resetProgress()

            val journeyState = repository.observeJourneyState().first()
            val firstTipState = repository.observeTipState(firstTipId).first()
            val storedProto = dataSource.journeyState.first()

            assertNull(journeyState.activeTipId)
            assertTrue(journeyState.tipStates.isEmpty())

            assertEquals(firstTipId, firstTipState.tipId)
            assertEquals(false, firstTipState.isBookmarked)
            assertEquals(TipCompletionStatus.NOT_STARTED, firstTipState.completionStatus)
            assertNull(firstTipState.lastViewedAtEpochMillis)
            assertNull(firstTipState.completedAtEpochMillis)

            assertEquals("", storedProto.activeTipId)
            assertTrue(storedProto.tipStatesList.isEmpty())
        }
    }

    private fun createRepository(
        dataSource: JourneyDataSource
    ): JourneyRepository {
        return JourneyRepositoryImpl(
            dataSource = dataSource,
            mapper = mapper
        )
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