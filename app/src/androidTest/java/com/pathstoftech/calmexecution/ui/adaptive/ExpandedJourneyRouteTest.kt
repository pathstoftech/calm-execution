package com.pathstoftech.calmexecution.ui.adaptive

import android.content.pm.ActivityInfo
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import com.pathstoftech.calmexecution.MainActivity
import org.junit.After
import org.junit.Rule
import org.junit.Test

/**
 * Integrated expanded route tests.
 *
 * Scope:
 * - Medium Tablet / expanded presentation;
 * - real app route wiring through MainActivity;
 * - feed selection -> selected detail pane;
 * - selected-detail completion action;
 * - clear selection back to empty detail placeholder.
 *
 * Out of scope:
 * - compact phone navigation;
 * - component-only list/detail layout rendering;
 * - full device-matrix coverage.
 */
class ExpandedJourneyRouteTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @After
    fun clearForcedOrientation() {
        composeRule.activityRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    @Test
    fun expandedRoute_selectionDetailActionAndClearSelection_workEndToEnd() {
        forceExpandedTabletLandscapeScope()

        waitUntilTagExists(EXPANDED_JOURNEY_ROUTE_TAG)
        waitUntilTagExists(EXPANDED_EMPTY_DETAIL_PLACEHOLDER_TAG)

        composeRule
            .onNodeWithTag(FIRST_TIP_CARD_TAG)
            .performScrollTo()
            .performClick()

        waitUntilTagExists(EXPANDED_SELECTED_DETAIL_FOCUS_TARGET_TAG)
        waitUntilTagExists(TIP_DETAIL_READY_STATE_TAG)

        composeRule
            .onNodeWithText("Define the real priority")
            .assertIsDisplayed()

        toggleSelectedDetailCompletionAndAssertStateChanged()

        composeRule
            .onNodeWithTag(EXPANDED_DETAIL_RETURN_ACTION_TAG)
            .assertIsDisplayed()
            .performClick()

        waitUntilTagExists(EXPANDED_EMPTY_DETAIL_PLACEHOLDER_TAG)

        composeRule
            .onNodeWithTag(EXPANDED_SELECTED_DETAIL_FOCUS_TARGET_TAG)
            .assertDoesNotExist()

        composeRule
            .onNodeWithTag(FIRST_TIP_CARD_STATUS_TAG, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    private fun forceExpandedTabletLandscapeScope() {
        composeRule.activityRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        composeRule.waitForIdle()
    }

    private fun toggleSelectedDetailCompletionAndAssertStateChanged() {
        composeRule
            .onNodeWithTag(TIP_DETAIL_READY_STATE_TAG)
            .performScrollToNode(hasTestTag(TIP_DETAIL_COMPLETE_ACTION_TAG))

        val initialStateDescription =
            composeRule
                .onNodeWithTag(TIP_DETAIL_COMPLETE_ACTION_TAG)
                .fetchSemanticsNode()
                .config[SemanticsProperties.StateDescription]

        val expectedStateDescription =
            if (initialStateDescription == COMPLETED_STATE_DESCRIPTION) {
                NOT_COMPLETED_STATE_DESCRIPTION
            } else {
                COMPLETED_STATE_DESCRIPTION
            }

        composeRule
            .onNodeWithTag(TIP_DETAIL_COMPLETE_ACTION_TAG)
            .performClick()

        waitUntilTagWithStateDescriptionExists(
            tag = TIP_DETAIL_COMPLETE_ACTION_TAG,
            stateDescription = expectedStateDescription,
        )

        composeRule
            .onNodeWithTag(TIP_DETAIL_COMPLETE_ACTION_TAG)
            .assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.StateDescription,
                    expectedStateDescription,
                ),
            )
    }

    private fun waitUntilTagExists(
        tag: String,
    ) {
        composeRule.waitUntil(
            timeoutMillis = DEFAULT_TIMEOUT_MILLIS,
        ) {
            composeRule
                .onAllNodesWithTag(tag, useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    private fun waitUntilTagWithStateDescriptionExists(
        tag: String,
        stateDescription: String,
    ) {
        composeRule.waitUntil(
            timeoutMillis = DEFAULT_TIMEOUT_MILLIS,
        ) {
            composeRule
                .onAllNodesWithTag(tag)
                .fetchSemanticsNodes()
                .any { node ->
                    node.config.getOrNull(SemanticsProperties.StateDescription) ==
                            stateDescription
                }
        }
    }

    private companion object {
        private const val DEFAULT_TIMEOUT_MILLIS = 5_000L

        private const val EXPANDED_JOURNEY_ROUTE_TAG = "expanded_journey_route"
        private const val EXPANDED_EMPTY_DETAIL_PLACEHOLDER_TAG =
            "expanded_empty_detail_placeholder"
        private const val EXPANDED_SELECTED_DETAIL_FOCUS_TARGET_TAG =
            "expanded_selected_detail_focus_target"
        private const val EXPANDED_DETAIL_RETURN_ACTION_TAG =
            "expanded_detail_return_action"

        private const val TIP_DETAIL_READY_STATE_TAG = "tip_detail_ready_state"
        private const val TIP_DETAIL_COMPLETE_ACTION_TAG = "tip_detail_complete_action"

        private const val FIRST_TIP_CARD_TAG = "tip_card_day_01_define_real_priority"
        private const val FIRST_TIP_CARD_STATUS_TAG =
            "tip_card_completion_status_day_01_define_real_priority"

        private const val COMPLETED_STATE_DESCRIPTION = "Completed"
        private const val NOT_COMPLETED_STATE_DESCRIPTION = "Not completed"
    }
}