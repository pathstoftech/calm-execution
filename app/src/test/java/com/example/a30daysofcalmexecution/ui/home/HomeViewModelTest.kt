package com.example.a30daysofcalmexecution.ui.home

import com.example.a30daysofcalmexecution.core.model.JourneyUserState
import com.example.a30daysofcalmexecution.core.model.SectionKey
import com.example.a30daysofcalmexecution.core.model.TipCompletionStatus
import com.example.a30daysofcalmexecution.core.model.TipUserState
import com.example.a30daysofcalmexecution.core.ui.AsyncStatus
import com.example.a30daysofcalmexecution.testing.FakeCatalogRepository
import com.example.a30daysofcalmexecution.testing.FakeJourneyRepository
import com.example.a30daysofcalmexecution.testing.FakePreferencesRepository
import com.example.a30daysofcalmexecution.testing.FakeTipImageResolver
import com.example.a30daysofcalmexecution.testing.MainDispatcherRule
import com.example.a30daysofcalmexecution.testing.ViewModelTestData
import com.example.a30daysofcalmexecution.testing.collectStateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun readyState_mapsCatalogJourneyAndPreferences() = runViewModelTest {
        val viewModel = createViewModel()

        val collectJob = collectStateFlow(viewModel.uiState)
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(AsyncStatus.READY, state.status)
        assertEquals("30 Days of Calm Execution", state.screenTitle)
        assertEquals("A calmer way to execute the day.", state.introText)
        assertEquals(2, state.journey.totalCount)
        assertEquals(0, state.journey.completedCount)
        assertEquals(1, state.journey.currentDay)
        assertEquals(ViewModelTestData.DayOneTipId, state.featuredTipId)
        assertNull(state.selectedTipId)
        assertNull(state.selectedTipDetail)
        assertEquals(1, state.feedSections.size)
        assertEquals(2, state.feedSections.first().items.size)

        collectJob.cancel()
    }

    @Test
    fun currentDay_usesFirstIncompleteTip_notActiveTip() = runViewModelTest {
        val journeyRepository = FakeJourneyRepository(
            initialState = JourneyUserState(
                activeTipId = ViewModelTestData.DayTwoTipId,
                tipStates = mapOf(
                    ViewModelTestData.DayTwoTipId to TipUserState(
                        tipId = ViewModelTestData.DayTwoTipId,
                        completionStatus = TipCompletionStatus.COMPLETED,
                        completedAtEpochMillis = 1L,
                    ),
                ),
            ),
        )

        val viewModel = createViewModel(
            journeyRepository = journeyRepository,
        )

        val collectJob = collectStateFlow(viewModel.uiState)
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(1, state.journey.completedCount)
        assertEquals(1, state.journey.currentDay)
        assertEquals(ViewModelTestData.DayOneTipId, state.featuredTipId)
        assertNull(state.selectedTipId)
        assertNull(state.selectedTipDetail)

        collectJob.cancel()
    }

    @Test
    fun selectSection_persistsPreferenceAndFiltersFeed() = runViewModelTest {
        val preferencesRepository = FakePreferencesRepository()
        val viewModel = createViewModel(
            preferencesRepository = preferencesRepository,
        )

        val collectJob = collectStateFlow(viewModel.uiState)
        advanceUntilIdle()

        viewModel.onAction(HomeAction.SelectSection(SectionKey.START_WITH_CLARITY))
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(listOf(SectionKey.START_WITH_CLARITY), preferencesRepository.selectedSectionMutations)
        assertEquals(SectionKey.START_WITH_CLARITY, state.selectedSection)
        assertEquals(1, state.feedSections.size)
        assertTrue(state.sectionTabs.first().isSelected)

        collectJob.cancel()
    }

    @Test
    fun toggleBookmark_mutatesRepositoryAndRecomposesState() = runViewModelTest {
        val journeyRepository = FakeJourneyRepository()
        val viewModel = createViewModel(
            journeyRepository = journeyRepository,
        )

        val collectJob = collectStateFlow(viewModel.uiState)
        advanceUntilIdle()

        viewModel.onAction(HomeAction.ToggleBookmark(ViewModelTestData.DayOneTipId))
        advanceUntilIdle()

        val card = viewModel.uiState.value.feedSections.first().items.first()

        assertEquals(listOf(ViewModelTestData.DayOneTipId to true), journeyRepository.bookmarkedMutations)
        assertTrue(card.isBookmarked)

        collectJob.cancel()
    }

    @Test
    fun toggleCompleted_mutatesRepositoryAndUpdatesProgress() = runViewModelTest {
        val journeyRepository = FakeJourneyRepository()
        val viewModel = createViewModel(
            journeyRepository = journeyRepository,
        )

        val collectJob = collectStateFlow(viewModel.uiState)
        advanceUntilIdle()

        viewModel.onAction(HomeAction.ToggleCompleted(ViewModelTestData.DayOneTipId))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        val card = state.feedSections.first().items.first()

        assertEquals(
            listOf(ViewModelTestData.DayOneTipId to TipCompletionStatus.COMPLETED),
            journeyRepository.completionMutations,
        )
        assertTrue(card.isCompleted)
        assertEquals(1, state.journey.completedCount)
        assertEquals(2, state.journey.currentDay)

        collectJob.cancel()
    }

    @Test
    fun catalogFailure_mapsToErrorState() = runViewModelTest {
        val catalogRepository = FakeCatalogRepository().apply {
            shouldThrowOnCatalogLoad = true
        }

        val viewModel = createViewModel(
            catalogRepository = catalogRepository,
        )

        val collectJob = collectStateFlow(viewModel.uiState)
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(AsyncStatus.ERROR, state.status)
        assertEquals("Unable to load journey content.", state.message?.text)
        assertTrue(state.feedSections.isEmpty())

        collectJob.cancel()
    }

    @Test
    fun selectExpandedDetail_setsSelectedTipDetail() = runViewModelTest {
        val viewModel = createViewModel()

        val collectJob = collectStateFlow(viewModel.uiState)
        advanceUntilIdle()

        viewModel.onAction(HomeAction.SelectExpandedDetail(ViewModelTestData.DayTwoTipId))
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(ViewModelTestData.DayTwoTipId, state.selectedTipId)
        assertEquals(ViewModelTestData.DayTwoTipId, state.selectedTipDetail?.id)
        assertEquals("Day 02", state.selectedTipDetail?.dayLabel)
        assertEquals("Stop planning by panic", state.selectedTipDetail?.title)

        collectJob.cancel()
    }

    @Test
    fun selectExpandedDetail_marksTipViewedWithoutAdvancingJourneyProgress() = runViewModelTest {
        val journeyRepository = FakeJourneyRepository()
        val viewModel = createViewModel(
            journeyRepository = journeyRepository,
        )

        val collectJob = collectStateFlow(viewModel.uiState)
        advanceUntilIdle()

        viewModel.onAction(HomeAction.SelectExpandedDetail(ViewModelTestData.DayTwoTipId))
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(listOf(ViewModelTestData.DayTwoTipId), journeyRepository.markedViewedTipIds)
        assertEquals(ViewModelTestData.DayTwoTipId, state.selectedTipId)
        assertEquals(ViewModelTestData.DayTwoTipId, state.selectedTipDetail?.id)
        assertEquals(0, state.journey.completedCount)
        assertEquals(1, state.journey.currentDay)
        assertEquals(ViewModelTestData.DayOneTipId, state.featuredTipId)

        collectJob.cancel()
    }

    @Test
    fun clearExpandedDetail_removesSelectedDetailWithoutChangingProgress() = runViewModelTest {
        val viewModel = createViewModel()

        val collectJob = collectStateFlow(viewModel.uiState)
        advanceUntilIdle()

        viewModel.onAction(HomeAction.SelectExpandedDetail(ViewModelTestData.DayTwoTipId))
        advanceUntilIdle()

        viewModel.onAction(HomeAction.SelectExpandedDetail(null))
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertNull(state.selectedTipId)
        assertNull(state.selectedTipDetail)
        assertEquals(0, state.journey.completedCount)
        assertEquals(1, state.journey.currentDay)
        assertEquals(ViewModelTestData.DayOneTipId, state.featuredTipId)

        collectJob.cancel()
    }

    private fun createViewModel(
        catalogRepository: FakeCatalogRepository = FakeCatalogRepository(),
        journeyRepository: FakeJourneyRepository = FakeJourneyRepository(),
        preferencesRepository: FakePreferencesRepository = FakePreferencesRepository(),
        tipImageResolver: FakeTipImageResolver = FakeTipImageResolver(),
    ): HomeViewModel =
        HomeViewModel(
            catalogRepository = catalogRepository,
            journeyRepository = journeyRepository,
            preferencesRepository = preferencesRepository,
            tipImageResolver = tipImageResolver,
        )

    private fun runViewModelTest(
        block: suspend TestScope.() -> Unit,
    ) = runTest(
        context = mainDispatcherRule.testDispatcher,
        testBody = block,
    )
}