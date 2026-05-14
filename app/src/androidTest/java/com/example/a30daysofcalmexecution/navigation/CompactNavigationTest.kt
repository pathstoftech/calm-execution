package com.example.a30daysofcalmexecution.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.example.a30daysofcalmexecution.MainActivity
import org.junit.Rule
import org.junit.Test

class CompactNavigationTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun appLaunch_startsOnHomeWithoutCompactBack() {
        waitForHome()

        composeRule.onNodeWithText("Journey progress").assertIsDisplayed()
        composeRule.onNodeWithTag(CompactBackActionTag).assertDoesNotExist()
    }

    @Test
    fun homeCardTap_opensDetail_andCompactBackReturnsHome() {
        openFirstTip()

        composeRule.onNodeWithTag(TipDetailReadyStateTag).assertIsDisplayed()
        composeRule.onNodeWithText("Define the real priority").assertIsDisplayed()
        composeRule.onNodeWithTag(CompactBackActionTag).assertIsDisplayed()

        composeRule.onNodeWithTag(CompactBackActionTag).performClick()

        waitForHome()

        composeRule.onNodeWithText("Journey progress").assertIsDisplayed()
        composeRule.onNodeWithTag(TipDetailReadyStateTag).assertDoesNotExist()
        composeRule.onNodeWithTag(CompactBackActionTag).assertDoesNotExist()
    }

    @Test
    fun systemBackFromDetail_returnsHome() {
        openFirstTip()

        composeRule.onNodeWithTag(TipDetailReadyStateTag).assertIsDisplayed()

        composeRule.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }

        waitForHome()

        composeRule.onNodeWithText("Journey progress").assertIsDisplayed()
        composeRule.onNodeWithTag(TipDetailReadyStateTag).assertDoesNotExist()
        composeRule.onNodeWithTag(CompactBackActionTag).assertDoesNotExist()
    }

    private fun openFirstTip() {
        waitForHome()
        showAllSectionsIfNeeded()

        composeRule
            .onNodeWithTag(FirstTipCardTag)
            .performScrollTo()
            .performClick()

        waitUntilTagExists(TipDetailReadyStateTag)
    }

    private fun waitForHome() {
        waitUntilTextExists("Journey progress")
    }

    private fun showAllSectionsIfNeeded() {
        val allNodes = composeRule
            .onAllNodesWithText("All")
            .fetchSemanticsNodes()

        if (allNodes.isNotEmpty()) {
            composeRule.onNodeWithText("All").performClick()
            composeRule.waitForIdle()
        }
    }

    private fun waitUntilTextExists(
        text: String,
    ) {
        composeRule.waitUntil(
            timeoutMillis = DefaultTimeoutMillis,
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
            timeoutMillis = DefaultTimeoutMillis,
        ) {
            composeRule
                .onAllNodesWithTag(tag)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    private companion object {
        const val DefaultTimeoutMillis = 10_000L
        const val CompactBackActionTag = "compact_back_action"
        const val TipDetailReadyStateTag = "tip_detail_ready_state"
        const val FirstTipCardTag = "tip_card_day_01_define_real_priority"
    }
}