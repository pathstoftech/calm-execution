package com.example.a30daysofcalmexecution.ui.adaptive

import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmExecutionTheme
import org.junit.Rule
import org.junit.Test

class ExpandedListDetailLayoutTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun expandedListDetailLayout_rendersListAndDetailPanes() {
        composeRule.setContent {
            CalmExecutionTheme(dynamicColor = false) {
                ExpandedListDetailLayout(
                    listPane = {
                        Text(
                            text = "List pane content",
                            modifier = Modifier.testTag(LIST_PANE_CONTENT_TAG),
                        )
                    },
                    detailPane = {
                        Text(
                            text = "Detail pane content",
                            modifier = Modifier.testTag(DETAIL_PANE_CONTENT_TAG),
                        )
                    },
                )
            }
        }

        composeRule.onNodeWithTag(ExpandedListDetailLayoutTestTag).assertIsDisplayed()
        composeRule.onNodeWithTag(ExpandedListPaneTestTag).assertIsDisplayed()
        composeRule.onNodeWithTag(ExpandedDetailPaneTestTag).assertIsDisplayed()

        composeRule
            .onNodeWithTag(ExpandedListPaneTestTag)
            .assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.PaneTitle,
                    "Journey feed",
                ),
            )

        composeRule
            .onNodeWithTag(ExpandedDetailPaneTestTag)
            .assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.PaneTitle,
                    "Tip detail",
                ),
            )

        composeRule.onNodeWithTag(LIST_PANE_CONTENT_TAG).assertIsDisplayed()
        composeRule.onNodeWithTag(DETAIL_PANE_CONTENT_TAG).assertIsDisplayed()
    }

    @Test
    fun emptyDetailPlaceholder_rendersGuidanceCopy() {
        composeRule.setContent {
            CalmExecutionTheme(dynamicColor = false) {
                ExpandedEmptyDetailPlaceholder()
            }
        }

        composeRule.onNodeWithTag(ExpandedEmptyDetailPlaceholderTestTag).assertIsDisplayed()
        composeRule.onNodeWithText("Detail").assertIsDisplayed()
        composeRule.onNodeWithText("Select a day to read").assertIsDisplayed()
        composeRule
            .onNodeWithText(
                "Choose any tip from the journey feed to keep the list visible while reading the full detail on this side.",
            )
            .assertIsDisplayed()
    }

    private companion object {
        const val LIST_PANE_CONTENT_TAG = "list_pane_content"
        const val DETAIL_PANE_CONTENT_TAG = "detail_pane_content"
    }
}