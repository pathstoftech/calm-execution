package com.pathstoftech.calmexecution.ui.adaptive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.pathstoftech.calmexecution.core.designsystem.component.CalmLabel
import com.pathstoftech.calmexecution.core.designsystem.component.CalmLabelTone
import com.pathstoftech.calmexecution.core.designsystem.theme.CalmTheme

@Composable
fun ExpandedListDetailLayout(
    listPane: @Composable () -> Unit,
    detailPane: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .testTag(ExpandedListDetailLayoutTestTag),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(
                    min = ExpandedListPaneMinWidth,
                    max = ExpandedListPaneMaxWidth,
                )
                .weight(ExpandedListPaneWeight)
                .testTag(ExpandedListPaneTestTag)
                .semantics {
                    paneTitle = "Journey feed"
                },
            color = CalmTheme.colorTokens.screenBackground,
            contentColor = CalmTheme.colorTokens.onScreenBackground,
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                listPane()
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxHeight()
                .weight(ExpandedDetailPaneWeight)
                .testTag(ExpandedDetailPaneTestTag)
                .semantics {
                    paneTitle = "Tip detail"
                },
            color = CalmTheme.colorTokens.screenBackground,
            contentColor = CalmTheme.colorTokens.onScreenBackground,
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                detailPane()
            }
        }
    }
}

@Composable
fun ExpandedEmptyDetailPlaceholder(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(CalmTheme.spacingTokens.screenPadding)
            .testTag(ExpandedEmptyDetailPlaceholderTestTag),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            shape = CalmTheme.shapeTokens.cardContainerLarge,
            color = CalmTheme.colorTokens.cardContainer,
            contentColor = CalmTheme.colorTokens.onCardContainer,
            tonalElevation = CalmTheme.elevationTokens.cardResting,
            shadowElevation = CalmTheme.elevationTokens.none,
        ) {
            Column(
                modifier = Modifier.padding(CalmTheme.spacingTokens.cardPadding),
                verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap),
                horizontalAlignment = Alignment.Start,
            ) {
                CalmLabel(
                    text = "Detail",
                    tone = CalmLabelTone.Primary,
                )

                Text(
                    text = "Select a day to read",
                    style = CalmTheme.typographyTokens.cardTitle,
                    color = CalmTheme.colorTokens.onCardContainer,
                )

                Text(
                    text = "Choose any tip from the journey feed to keep the list visible while reading the full detail on this side.",
                    style = CalmTheme.typographyTokens.cardBody,
                    color = CalmTheme.colorTokens.onCardContainerVariant,
                )
            }
        }
    }
}

const val ExpandedEmptyDetailPlaceholderTestTag = "expanded_empty_detail_placeholder"
private val ExpandedListPaneMinWidth = 320.dp
private val ExpandedListPaneMaxWidth = 480.dp
private const val ExpandedListPaneWeight = 0.42f
private const val ExpandedDetailPaneWeight = 0.58f

const val ExpandedListDetailLayoutTestTag = "expanded_list_detail_layout"
const val ExpandedListPaneTestTag = "expanded_list_pane"
const val ExpandedDetailPaneTestTag = "expanded_detail_pane"