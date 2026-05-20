package com.example.a30daysofcalmexecution.ui.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.example.a30daysofcalmexecution.R
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmExecutionTheme
import com.example.a30daysofcalmexecution.core.model.TipCompletionStatus
import com.example.a30daysofcalmexecution.core.model.TipId
import com.example.a30daysofcalmexecution.core.ui.AsyncStatus
import com.example.a30daysofcalmexecution.core.ui.UiMessage
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class TipDetailScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun loadingState_rendersLoadingBranch() {
        setDetailContent(
            state = TipDetailUiState(
                status = AsyncStatus.LOADING,
            ),
        )

        composeRule
            .onNodeWithTag("tip_detail_loading_state")
            .assertIsDisplayed()
    }

    @Test
    fun errorState_rendersSafeErrorAndRetryAction() {
        val actions = mutableListOf<TipDetailAction>()
        var backCalls = 0

        setDetailContent(
            state = TipDetailUiState(
                status = AsyncStatus.ERROR,
                message = UiMessage(
                    id = 1L,
                    text = "This tip is not available right now.",
                ),
            ),
            actions = actions,
            onBack = {
                backCalls += 1
            },
        )

        composeRule.onNodeWithTag("tip_detail_error_state").assertIsDisplayed()
        composeRule.onNodeWithText("Unable to open tip").assertIsDisplayed()
        composeRule.onNodeWithText("This tip is not available right now.").assertIsDisplayed()

        composeRule.onNodeWithText("Try again").performClick()

        assertEquals(
            listOf(TipDetailAction.RetryLoad),
            actions,
        )

        composeRule.onNodeWithText("Back to journey").performClick()

        assertEquals(
            1,
            backCalls,
        )
    }

    @Test
    fun readyState_rendersMetaContentAndActions() {
        setDetailContent(
            state = readyDetailState(),
        )

        composeRule.onNodeWithTag("tip_detail_ready_state").assertIsDisplayed()

        composeRule
            .onNodeWithContentDescription("A calm planning scene", substring = true)
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("A calm planning scene", substring = true)
            .assertDoesNotExist()
        composeRule.onNodeWithText("Day 02").assertIsDisplayed()
        composeRule.onNodeWithText("Stop planning by panic").assertIsDisplayed()
        composeRule.onNodeWithText("Clarity").assertIsDisplayed()

        composeRule
            .onNodeWithTag("tip_detail_ready_state")
            .performScrollToNode(hasText("Problem"))

        composeRule.onNodeWithText("Problem").assertIsDisplayed()
        composeRule.onNodeWithText("Name the real pressure before making the plan.").assertIsDisplayed()

        composeRule
            .onNodeWithTag("tip_detail_ready_state")
            .performScrollToNode(hasText("Tip"))

        composeRule.onNodeWithText("Tip").assertIsDisplayed()
        composeRule.onNodeWithText("Write the next calm action before adding more tasks.").assertIsDisplayed()

        composeRule
            .onNodeWithTag("tip_detail_ready_state")
            .performScrollToNode(hasText("Why it helps"))

        composeRule.onNodeWithText("Why it helps").assertIsDisplayed()
        composeRule.onNodeWithText("A named pressure is easier to manage than a vague threat.").assertIsDisplayed()

        composeRule
            .onNodeWithTag("tip_detail_ready_state")
            .performScrollToNode(hasText("Try today"))

        composeRule.onNodeWithText("Try today").assertIsDisplayed()
        composeRule.onNodeWithText("Before planning, write one sentence that starts with: The real risk is...").assertIsDisplayed()

        composeRule
            .onNodeWithTag("tip_detail_ready_state")
            .performScrollToNode(hasText("Bookmark"))

        composeRule.onNodeWithText("Bookmark").assertIsDisplayed()
        composeRule.onNodeWithText("Mark complete").assertIsDisplayed()
    }

    @Test
    fun bookmarkClick_emitsToggleBookmark() {
        val actions = mutableListOf<TipDetailAction>()

        setDetailContent(
            state = readyDetailState(),
            actions = actions,
        )

        composeRule
            .onNodeWithTag("tip_detail_ready_state")
            .performScrollToNode(hasText("Bookmark"))

        composeRule.onNodeWithText("Bookmark").performClick()

        assertEquals(
            listOf(TipDetailAction.ToggleBookmark),
            actions,
        )
    }

    @Test
    fun completionClick_emitsToggleCompleted() {
        val actions = mutableListOf<TipDetailAction>()

        setDetailContent(
            state = readyDetailState(),
            actions = actions,
        )

        composeRule
            .onNodeWithTag("tip_detail_ready_state")
            .performScrollToNode(hasText("Mark complete"))

        composeRule.onNodeWithText("Mark complete").performClick()

        assertEquals(
            listOf(TipDetailAction.ToggleCompleted),
            actions,
        )
    }

    @Test
    fun bookmarkedAndCompletedState_rendersActionLabels() {
        setDetailContent(
            state = readyDetailState(
                tip = sampleTipDetail(
                    isBookmarked = true,
                    completionStatus = TipCompletionStatus.COMPLETED,
                ),
            ),
        )

        composeRule
            .onNodeWithTag("tip_detail_ready_state")
            .performScrollToNode(hasText("Bookmarked"))

        composeRule.onNodeWithText("Bookmarked").assertIsDisplayed()
        composeRule.onNodeWithText("Completed").assertIsDisplayed()
    }

    @Test
    fun readyStateWithNullTip_rendersSafeError() {
        setDetailContent(
            state = TipDetailUiState(
                status = AsyncStatus.READY,
                tip = null,
            ),
        )

        composeRule.onNodeWithText("Unable to open tip").assertIsDisplayed()
        composeRule.onNodeWithText("This tip is not available right now.").assertIsDisplayed()
    }

    private fun setDetailContent(
        state: TipDetailUiState,
        actions: MutableList<TipDetailAction> = mutableListOf(),
        onBack: () -> Unit = {},
    ) {
        composeRule.setContent {
            CalmExecutionTheme(
                dynamicColor = false,
            ) {
                TipDetailScreen(
                    state = state,
                    onAction = { action ->
                        actions += action
                    },
                    onBack = onBack,
                )
            }
        }
    }

    private fun readyDetailState(
        tip: TipDetailUi = sampleTipDetail(),
    ): TipDetailUiState =
        TipDetailUiState(
            status = AsyncStatus.READY,
            screenTitle = tip.title,
            tip = tip,
            message = null,
        )

    private fun sampleTipDetail(
        isBookmarked: Boolean = false,
        completionStatus: TipCompletionStatus = TipCompletionStatus.NOT_STARTED,
    ): TipDetailUi =
        TipDetailUi(
            id = TipId("day_02_stop_planning_by_panic"),
            dayLabel = "Day 02",
            title = "Stop planning by panic",
            categoryLabel = "Clarity",
            imageKey = "tip_02_stop_planning_by_panic",
            imageResId = R.drawable.tip_02_stop_planning_by_panic,
            imageContentDescription = "A calm planning scene",
            imageDecorative = false,
            problem = TipDetailTextSectionUi(
                title = "Problem",
                body = "Name the real pressure before making the plan.",
            ),
            recommendation = TipDetailTextSectionUi(
                title = "Tip",
                body = "Write the next calm action before adding more tasks.",
            ),
            whyItHelps = TipDetailTextSectionUi(
                title = "Why it helps",
                body = "A named pressure is easier to manage than a vague threat.",
            ),
            tryToday = TipDetailTextSectionUi(
                title = "Try today",
                body = "Before planning, write one sentence that starts with: The real risk is...",
            ),
            isBookmarked = isBookmarked,
            completionStatus = completionStatus,
        )
}