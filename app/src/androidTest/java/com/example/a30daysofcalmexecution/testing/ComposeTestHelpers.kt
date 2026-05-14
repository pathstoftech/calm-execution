package com.example.a30daysofcalmexecution.testing

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmExecutionTheme

fun ComposeContentTestRule.setCalmContent(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    reducedMotion: Boolean = true,
    content: @Composable () -> Unit,
) {
    setContent {
        CalmExecutionTheme(
            darkTheme = darkTheme,
            dynamicColor = dynamicColor,
            reducedMotion = reducedMotion,
            content = content,
        )
    }
}

fun ComposeContentTestRule.waitUntilTextExists(
    text: String,
    timeoutMillis: Long = DefaultUiTestTimeoutMillis,
) {
    waitUntil(
        timeoutMillis = timeoutMillis,
    ) {
        onAllNodesWithText(text)
            .fetchSemanticsNodes()
            .isNotEmpty()
    }
}

fun ComposeContentTestRule.waitUntilTagExists(
    tag: String,
    timeoutMillis: Long = DefaultUiTestTimeoutMillis,
) {
    waitUntil(
        timeoutMillis = timeoutMillis,
    ) {
        onAllNodesWithTag(tag)
            .fetchSemanticsNodes()
            .isNotEmpty()
    }
}

fun ComposeContentTestRule.waitUntilTextDoesNotExist(
    text: String,
    timeoutMillis: Long = DefaultUiTestTimeoutMillis,
) {
    waitUntil(
        timeoutMillis = timeoutMillis,
    ) {
        onAllNodesWithText(text)
            .fetchSemanticsNodes()
            .isEmpty()
    }
}

fun ComposeContentTestRule.waitUntilTagDoesNotExist(
    tag: String,
    timeoutMillis: Long = DefaultUiTestTimeoutMillis,
) {
    waitUntil(
        timeoutMillis = timeoutMillis,
    ) {
        onAllNodesWithTag(tag)
            .fetchSemanticsNodes()
            .isEmpty()
    }
}

const val DefaultUiTestTimeoutMillis = 10_000L