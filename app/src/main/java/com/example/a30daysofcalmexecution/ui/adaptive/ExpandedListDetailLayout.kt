package com.example.a30daysofcalmexecution.ui.adaptive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmTheme

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
                .testTag(ExpandedListPaneTestTag),
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
                .testTag(ExpandedDetailPaneTestTag),
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
fun ExpandedBlankDetailPane(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag(ExpandedBlankDetailPaneTestTag),
    )
}

const val ExpandedBlankDetailPaneTestTag = "expanded_blank_detail_pane"

private val ExpandedListPaneMinWidth = 320.dp
private val ExpandedListPaneMaxWidth = 480.dp
private const val ExpandedListPaneWeight = 0.42f
private const val ExpandedDetailPaneWeight = 0.58f

const val ExpandedListDetailLayoutTestTag = "expanded_list_detail_layout"
const val ExpandedListPaneTestTag = "expanded_list_pane"
const val ExpandedDetailPaneTestTag = "expanded_detail_pane"