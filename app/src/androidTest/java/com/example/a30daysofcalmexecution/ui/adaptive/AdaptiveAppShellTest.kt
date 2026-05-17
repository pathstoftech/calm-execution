package com.example.a30daysofcalmexecution.ui.adaptive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmExecutionTheme
import org.junit.Rule
import org.junit.Test

class AdaptiveAppShellTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun compactWidth_rendersCompactContent() {
        setAdaptiveContent(width = 839.dp)

        composeRule.onNodeWithTag(COMPACT_CONTENT_TAG).assertIsDisplayed()
        composeRule.onNodeWithTag(EXPANDED_CONTENT_TAG).assertDoesNotExist()
    }

    @Test
    fun expandedWidth_rendersExpandedContent() {
        setAdaptiveContent(width = 840.dp)

        composeRule.onNodeWithTag(EXPANDED_CONTENT_TAG).assertIsDisplayed()
        composeRule.onNodeWithTag(COMPACT_CONTENT_TAG).assertDoesNotExist()
    }

    @Test
    fun expandedAppScaffold_rendersContentInsideExpandedScaffold() {
        composeRule.setContent {
            CalmExecutionTheme(dynamicColor = false) {
                ExpandedAppScaffold {
                    Text(
                        text = "Expanded scaffold content",
                        modifier = Modifier.testTag(EXPANDED_SCAFFOLD_CONTENT_TAG),
                    )
                }
            }
        }

        composeRule.onNodeWithTag(ExpandedAppScaffoldTestTag).assertIsDisplayed()
        composeRule.onNodeWithTag(EXPANDED_SCAFFOLD_CONTENT_TAG).assertIsDisplayed()
    }

    private fun setAdaptiveContent(
        width: Dp,
        height: Dp = 600.dp,
    ) {
        composeRule.setContent {
            CalmExecutionTheme(dynamicColor = false) {
                Box(
                    modifier = Modifier.size(
                        width = width,
                        height = height,
                    ),
                ) {
                    AdaptiveAppShell(
                        compactContent = {
                            Text(
                                text = "Compact content",
                                modifier = Modifier.testTag(COMPACT_CONTENT_TAG),
                            )
                        },
                        expandedContent = {
                            Text(
                                text = "Expanded content",
                                modifier = Modifier.testTag(EXPANDED_CONTENT_TAG),
                            )
                        },
                    )
                }
            }
        }
    }

    private companion object {
        const val COMPACT_CONTENT_TAG = "compact_content"
        const val EXPANDED_CONTENT_TAG = "expanded_content"
        const val EXPANDED_SCAFFOLD_CONTENT_TAG = "expanded_scaffold_content"
    }
}