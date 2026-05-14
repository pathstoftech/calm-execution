package com.example.a30daysofcalmexecution.ui.detail

import androidx.lifecycle.SavedStateHandle
import com.example.a30daysofcalmexecution.core.model.JourneyUserState
import com.example.a30daysofcalmexecution.core.model.TipCompletionStatus
import com.example.a30daysofcalmexecution.core.model.TipUserState
import com.example.a30daysofcalmexecution.core.ui.AsyncStatus
import com.example.a30daysofcalmexecution.testing.FakeCatalogRepository
import com.example.a30daysofcalmexecution.testing.FakeJourneyRepository
import com.example.a30daysofcalmexecution.testing.MainDispatcherRule
import com.example.a30daysofcalmexecution.testing.ViewModelTestData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TipDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun validTip_mapsCatalogAndUserStateToReadyUiState() = runViewModelTest {
        val journeyRepository = FakeJourneyRepository(
            initialState = JourneyUserState(
                tipStates = mapOf(
                    ViewModelTestData.DayOneTipId to TipUserState(
                        tipId = ViewModelTestData.DayOneTipId,
                        isBookmarked = true,
                        completionStatus = TipCompletionStatus.COMPLETED,
                        completedAtEpochMillis = 1L,
                    ),
                ),
            ),
        )

        val viewModel = createViewModel(
            journeyRepository = journeyRepository,
        )

        val collectJob = collect(viewModel.uiState)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        val tip = requireNotNull(state.tip)

        assertEquals(AsyncStatus.READY, state.status)
        assertEquals("Define real priority", state.screenTitle)
        assertEquals(ViewModelTestData.DayOneTipId, tip.id)
        assertEquals("Day 01", tip.dayLabel)
        assertEquals("Define real priority", tip.title)
        assertEquals("Planning", tip.categoryLabel)
        assertEquals("Problem", tip.problem.title)
        assertEquals("Tip", tip.recommendation.title)
        assertEquals("Why it helps", tip.whyItHelps.title)
        assertEquals("Try today", tip.tryToday.title)
        assertTrue(tip.isBookmarked)
        assertTrue(tip.isCompleted)

        collectJob.cancel()
    }

    @Test
    fun validTip_marksViewedOnOpen() = runViewModelTest {
        val journeyRepository = FakeJourneyRepository()
        val viewModel = createViewModel(
            journeyRepository = journeyRepository,
        )

        val collectJob = collect(viewModel.uiState)
        advanceUntilIdle()

        assertEquals(listOf(ViewModelTestData.DayOneTipId), journeyRepository.markedViewedTipIds)

        collectJob.cancel()
    }

    @Test
    fun missingTip_mapsToSafeErrorAndDoesNotMarkViewed() = runViewModelTest {
        val journeyRepository = FakeJourneyRepository()
        val viewModel = createViewModel(
            tipIdValue = ViewModelTestData.MissingTipId.value,
            journeyRepository = journeyRepository,
        )

        val collectJob = collect(viewModel.uiState)
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(AsyncStatus.ERROR, state.status)
        assertNull(state.tip)
        assertEquals("Tip detail", state.screenTitle)
        assertTrue(
            state.message?.text?.contains("not available") == true,
        )
        assertTrue(journeyRepository.markedViewedTipIds.isEmpty())

        collectJob.cancel()
    }

    @Test
    fun tipLoadFailure_mapsToSafeError() = runViewModelTest {
        val catalogRepository = FakeCatalogRepository().apply {
            shouldThrowOnTipLoad = true
        }

        val viewModel = createViewModel(
            catalogRepository = catalogRepository,
        )

        val collectJob = collect(viewModel.uiState)
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(AsyncStatus.ERROR, state.status)
        assertNull(state.tip)
        assertEquals("Unable to load tip detail.", state.message?.text)

        collectJob.cancel()
    }

    @Test
    fun toggleBookmark_mutatesRepositoryAndRecomposesState() = runViewModelTest {
        val journeyRepository = FakeJourneyRepository()
        val viewModel = createViewModel(
            journeyRepository = journeyRepository,
        )

        val collectJob = collect(viewModel.uiState)
        advanceUntilIdle()

        viewModel.onAction(TipDetailAction.ToggleBookmark)
        advanceUntilIdle()

        val tip = requireNotNull(viewModel.uiState.value.tip)

        assertEquals(listOf(ViewModelTestData.DayOneTipId to true), journeyRepository.bookmarkedMutations)
        assertTrue(tip.isBookmarked)

        collectJob.cancel()
    }

    @Test
    fun toggleCompleted_mutatesRepositoryAndRecomposesState() = runViewModelTest {
        val journeyRepository = FakeJourneyRepository()
        val viewModel = createViewModel(
            journeyRepository = journeyRepository,
        )

        val collectJob = collect(viewModel.uiState)
        advanceUntilIdle()

        viewModel.onAction(TipDetailAction.ToggleCompleted)
        advanceUntilIdle()

        val tip = requireNotNull(viewModel.uiState.value.tip)

        assertEquals(
            listOf(ViewModelTestData.DayOneTipId to TipCompletionStatus.COMPLETED),
            journeyRepository.completionMutations,
        )
        assertTrue(tip.isCompleted)

        collectJob.cancel()
    }

    @Test
    fun toggleCompletedAgain_setsNotStarted() = runViewModelTest {
        val journeyRepository = FakeJourneyRepository(
            initialState = JourneyUserState(
                tipStates = mapOf(
                    ViewModelTestData.DayOneTipId to TipUserState(
                        tipId = ViewModelTestData.DayOneTipId,
                        completionStatus = TipCompletionStatus.COMPLETED,
                        completedAtEpochMillis = 1L,
                    ),
                ),
            ),
        )

        val viewModel = createViewModel(
            journeyRepository = journeyRepository,
        )

        val collectJob = collect(viewModel.uiState)
        advanceUntilIdle()

        viewModel.onAction(TipDetailAction.ToggleCompleted)
        advanceUntilIdle()

        val tip = requireNotNull(viewModel.uiState.value.tip)

        assertEquals(
            ViewModelTestData.DayOneTipId to TipCompletionStatus.NOT_STARTED,
            journeyRepository.completionMutations.last(),
        )
        assertFalse(tip.isCompleted)

        collectJob.cancel()
    }

    private fun createViewModel(
        tipIdValue: String = ViewModelTestData.DayOneTipId.value,
        catalogRepository: FakeCatalogRepository = FakeCatalogRepository(),
        journeyRepository: FakeJourneyRepository = FakeJourneyRepository(),
    ): TipDetailViewModel =
        TipDetailViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf("tipId" to tipIdValue),
            ),
            catalogRepository = catalogRepository,
            journeyRepository = journeyRepository,
        )

    private fun TestScope.collect(
        stateFlow: StateFlow<TipDetailUiState>,
    ): Job =
        launch {
            stateFlow.collect {}
        }

    private fun runViewModelTest(
        block: suspend TestScope.() -> Unit,
    ) = runTest(
        context = mainDispatcherRule.testDispatcher,
        testBody = block,
    )
}