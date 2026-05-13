package com.example.a30daysofcalmexecution.ui.home

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmExecutionTheme
import com.example.a30daysofcalmexecution.core.model.SectionKey
import com.example.a30daysofcalmexecution.core.model.TipId
import com.example.a30daysofcalmexecution.core.ui.AsyncStatus
import com.example.a30daysofcalmexecution.core.ui.UiMessage
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun readyState_renderHomeContent() {
        setHomeContent(
            state = readyHomeState()
        )

        composeRule.onNodeWithText("A calmer way to execute the day.").assertIsDisplayed()
        composeRule.onNodeWithText("Journey progress").assertIsDisplayed()
        composeRule.onAllNodesWithText("Day 01").assertCountEquals(2)
        composeRule.onNodeWithText("1 of 30 days complete").assertIsDisplayed()
        composeRule.onNodeWithText("3%").assertIsDisplayed()
        composeRule.onAllNodesWithText("Start with Clarity").assertCountEquals(2)
        composeRule.onNodeWithText("Define real priority").assertIsDisplayed()
        composeRule.onNodeWithText("Choose the one result that matters before opening the noise gates.").assertIsDisplayed()
    }

    @Test
    fun loadingState_doesNotRenderReadyContent() {
        setHomeContent(
            state = HomeUiState(
                status = AsyncStatus.LOADING
            )
        )

        composeRule.onNodeWithText("Journey progress").assertDoesNotExist()
        composeRule.onNodeWithText("Define real priority").assertDoesNotExist()
    }

    @Test
    fun errorState_rendersErrorAndRetryAction() {
        val actions = mutableListOf<HomeAction>()

        setHomeContent(
            state = HomeUiState(
                status = AsyncStatus.ERROR,
                message = UiMessage(
                    id = 1L,
                    text = "Unable to load journey content."
                )
            ),
            actions = actions
        )

        composeRule.onNodeWithText("Unable to load Home").assertIsDisplayed()
        composeRule.onNodeWithText("Unable to load journey content.").assertIsDisplayed()

        composeRule.onNodeWithText("Try again").performClick()

        assertEquals(
            listOf(HomeAction.RetryLoad),
            actions
        )
    }

    @Test
    fun selectionChipClick_emitSelectSection() {
        val actions = mutableListOf<HomeAction>()

        setHomeContent(
            state = readyHomeState(),
            actions = actions
        )

        composeRule.onNodeWithText("Build Focus").performClick()

        assertEquals(
            listOf(HomeAction.SelectSection(SectionKey.BUILD_FOCUS)),
            actions
        )
    }

    @Test
    fun allChipClick_emitsSelectSectionNull() {
        val actions = mutableListOf<HomeAction>()

        setHomeContent(
            state = readyHomeState(
                selectedSection = SectionKey.BUILD_FOCUS
            ),
            actions = actions
        )

        composeRule.onNodeWithText("All").performClick()

        assertEquals(
            listOf(HomeAction.SelectSection(null)),
            actions
        )
    }

    @Test
    fun emptyFilteredState_renderShowAllAction() {
        val actions = mutableListOf<HomeAction>()

        setHomeContent(
            state = readyHomeState(
                selectedSection = SectionKey.BUILD_FOCUS,
                feedSections = listOf(
                    HomeFeedSectionUi(
                        key = SectionKey.BUILD_FOCUS,
                        title = "Build Focus",
                        items = emptyList()
                    )
                )
            ),
            actions = actions
        )

        composeRule.onNodeWithText("No tips in this section").assertIsDisplayed()
        composeRule.onNodeWithText("Try another phase or return to the full 30-day journey.").assertIsDisplayed()

        composeRule.onNodeWithText("Show all").performClick()

        assertEquals(
            listOf(HomeAction.SelectSection(null)),
            actions
        )
    }

    @Test
    fun tipCardClick_emitsOpenTip() {
        val actions = mutableListOf<HomeAction>()

        setHomeContent(
            state = readyHomeState(),
            actions = actions
        )

        composeRule.onNodeWithText("Define real priority").performClick()

        assertEquals(
            listOf(HomeAction.OpenTip(DayOneTipId)),
            actions
        )
    }

    @Test
    fun bookmarkClick_emitsToggleBookmark() {
        val actions = mutableListOf<HomeAction>()

        setHomeContent(
            state = readyHomeState(),
            actions = actions,
        )

        composeRule.onNodeWithText("Bookmark").performClick()

        assertEquals(
            listOf(HomeAction.ToggleBookmark(DayOneTipId)),
            actions,
        )
    }

    @Test
    fun completionClick_emitsToggleCompleted() {
        val actions = mutableListOf<HomeAction>()

        setHomeContent(
            state = readyHomeState(),
            actions = actions,
        )

        composeRule.onNodeWithText("Mark complete").performClick()

        assertEquals(
            listOf(HomeAction.ToggleCompleted(DayOneTipId)),
            actions,
        )
    }

    @Test
    fun completedAndBookmarkedState_rendersAffordanceLabels() {
        setHomeContent(
            state = readyHomeState(
                feedSections = listOf(
                    HomeFeedSectionUi(
                        key = SectionKey.START_WITH_CLARITY,
                        title = "Start with Clarity",
                        items = listOf(
                            sampleTipCard(
                                isBookmarked = true,
                                isCompleted = true,
                            ),
                        ),
                    ),
                ),
            ),
        )

        composeRule.onNodeWithText("Bookmarked").assertIsDisplayed()
        composeRule.onNodeWithText("Completed").assertIsDisplayed()
    }

    private fun setHomeContent(
        state: HomeUiState,
        actions: MutableList<HomeAction> = mutableListOf()
    ) {
        composeRule.setContent {
            CalmExecutionTheme(
                dynamicColor = false
            ) {
                HomeScreen(
                    state = state,
                    onAction = { action ->
                        actions += action
                    }
                )
            }
        }
    }

    private fun readyHomeState(
        selectedSection: SectionKey? = null,
        feedSections: List<HomeFeedSectionUi> = listOf(
            HomeFeedSectionUi(
                key = SectionKey.START_WITH_CLARITY,
                title = "Start with Clarity",
                items = listOf(sampleTipCard())
            )
        )
    ): HomeUiState =
        HomeUiState(
            status = AsyncStatus.READY,
            introText = "A calmer way to execute the day.",
            journey = JourneyProgressUi(
                completedCount = 1,
                totalCount = 30,
                currentDay = 1,
                completionFraction = 1f / 30f
            ),
            selectedSection = selectedSection,
            sectionTabs = listOf(
                SectionTabUi(
                    key = SectionKey.START_WITH_CLARITY,
                    title = "Start with Clarity",
                    isSelected = selectedSection == SectionKey.START_WITH_CLARITY,
                    completedCount = 1,
                    totalCount = 6
                ),
                SectionTabUi(
                    key = SectionKey.BUILD_FOCUS,
                    title = "Build Focus",
                    isSelected = selectedSection == SectionKey.BUILD_FOCUS,
                    completedCount = 0,
                    totalCount = 6,
                ),
            ),
            feedSections = feedSections,
            featuredTipId = DayOneTipId,
            selectedTipId = DayOneTipId
        )
    private fun sampleTipCard(
        isBookmarked: Boolean = false,
        isCompleted: Boolean = false
    ): TipCardUi =
        TipCardUi(
            id = DayOneTipId,
            dayLabel = "Day 01",
            title = "Define real priority",
            previewText = "Choose the one result that matters before opening the noise gates.",
            categoryLabel = "Clarity",
            imageKey = "day_01_define_real_priority",
            isCompleted = isCompleted,
            isBookmarked = isBookmarked
        )
    private companion object {
        val DayOneTipId = TipId("day_01_define_real_priority")
    }
}