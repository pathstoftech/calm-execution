package com.pathstoftech.calmexecution.navigation

import android.content.pm.ActivityInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.pathstoftech.calmexecution.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Compact navigation tests for the phone/compact presentation.
 *
 * Scope:
 * - compact-width app navigation behavior;
 * - portrait phone emulator execution;
 * - Home -> Detail -> Home transitions;
 * - compact back affordance behavior;
 * - system Back behavior from Detail.
 *
 * Out of scope:
 * - expanded/tablet list-detail behavior;
 * - landscape-specific layout guarantees;
 * - adaptive breakpoint verification.
 *
 * Expanded and adaptive behavior is covered by the adaptive test bucket.
 */
class CompactNavigationTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun forceCompactPortraitScope() {
        composeRule.activityRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        composeRule.waitForIdle()
    }

    @After
    fun clearForcedOrientation() {
        composeRule.activityRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    @Test
    fun appLaunch_startsOnHomeWithoutCompactBack() {
        waitForHome()

        composeRule
            .onNodeWithText("Journey progress")
            .assertIsDisplayed()

        composeRule
            .onNodeWithTag(COMPACT_BACK_ACTION_TAG)
            .assertDoesNotExist()
    }

    @Test
    fun appLaunch_showsSettingsIconAction() {
        waitForHome()

        composeRule
            .onNodeWithTag(COMPACT_SETTINGS_ACTION_TAG)
            .assertIsDisplayed()

        composeRule
            .onNodeWithContentDescription("Open settings")
            .assertIsDisplayed()
    }

    @Test
    fun homeCardTap_opensDetail_andCompactBackReturnsHome() {
        openFirstTip()

        composeRule
            .onNodeWithTag(TIP_DETAIL_READY_STATE_TAG)
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Define the real priority")
            .assertIsDisplayed()

        composeRule
            .onNodeWithTag(COMPACT_BACK_ACTION_TAG)
            .assertIsDisplayed()

        composeRule
            .onNodeWithTag(COMPACT_BACK_ACTION_TAG)
            .performClick()

        waitForHome()

        composeRule
            .onNodeWithText("Journey progress")
            .assertIsDisplayed()

        composeRule
            .onNodeWithTag(TIP_DETAIL_READY_STATE_TAG)
            .assertDoesNotExist()

        composeRule
            .onNodeWithTag(COMPACT_BACK_ACTION_TAG)
            .assertDoesNotExist()
    }

    @Test
    fun systemBackFromDetail_returnsHome() {
        openFirstTip()

        composeRule
            .onNodeWithTag(TIP_DETAIL_READY_STATE_TAG)
            .assertIsDisplayed()

        composeRule.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }

        waitForHome()

        composeRule
            .onNodeWithText("Journey progress")
            .assertIsDisplayed()

        composeRule
            .onNodeWithTag(TIP_DETAIL_READY_STATE_TAG)
            .assertDoesNotExist()

        composeRule
            .onNodeWithTag(COMPACT_BACK_ACTION_TAG)
            .assertDoesNotExist()
    }

    @Test
    fun detail_showsBackIconAction() {
        openFirstTip()

        composeRule
            .onNodeWithTag(COMPACT_BACK_ACTION_TAG)
            .assertIsDisplayed()

        composeRule
            .onNodeWithContentDescription("Navigate back")
            .assertIsDisplayed()
    }

    private fun openFirstTip() {
        waitForHome()
        showAllSectionsIfNeeded()

        composeRule
            .onNodeWithTag(FIRST_TIP_CARD_TAG)
            .performScrollTo()
            .performClick()

        waitUntilTagExists(TIP_DETAIL_READY_STATE_TAG)
    }

    private fun waitForHome() {
        waitUntilTextExists("Journey progress")
    }

    private fun showAllSectionsIfNeeded() {
        val allNodes = composeRule
            .onAllNodesWithText("All")
            .fetchSemanticsNodes()

        if (allNodes.isNotEmpty()) {
            composeRule
                .onNodeWithText("All")
                .performClick()

            composeRule.waitForIdle()
        }
    }

    private fun waitUntilTextExists(
        text: String,
    ) {
        composeRule.waitUntil(
            timeoutMillis = DEFAULT_TIMEOUT_MILLIS,
        ) {
            composeRule
                .onAllNodesWithText(text)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    private fun waitUntilTagExists(
        tag: String,
    ) {
        composeRule.waitUntil(
            timeoutMillis = DEFAULT_TIMEOUT_MILLIS,
        ) {
            composeRule
                .onAllNodesWithTag(tag)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    private companion object {
        private const val DEFAULT_TIMEOUT_MILLIS = 5_000L
        private const val COMPACT_BACK_ACTION_TAG = "compact_back_action"
        private const val COMPACT_SETTINGS_ACTION_TAG = "compact_settings_action"
        private const val TIP_DETAIL_READY_STATE_TAG = "tip_detail_ready_state"
        private const val FIRST_TIP_CARD_TAG = "tip_card_day_01_define_real_priority"
    }
}