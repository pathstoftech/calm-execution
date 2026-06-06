package com.example.a30daysofcalmexecution.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmErrorPanel
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmLoadingPanel
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmTheme
import com.example.a30daysofcalmexecution.core.ui.AsyncStatus

@Composable
fun TipDetailScreen(
    state: TipDetailUiState,
    onAction: (TipDetailAction) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (state.status) {
        AsyncStatus.IDLE,
        AsyncStatus.LOADING -> {
            TipDetailLoadingState(modifier = modifier)
        }

        AsyncStatus.ERROR -> {
            TipDetailErrorState(
                message = state.message?.text ?: "This tip is not available right now.",
                onRetry = {
                    onAction(TipDetailAction.RetryLoad)
                },
                onBack = onBack,
                modifier = modifier
            )
        }

        AsyncStatus.READY -> {
            val tip = state.tip

            if (tip == null) {
                TipDetailErrorState(
                    message = "This tip is not available right now.",
                    onRetry = {
                        onAction(TipDetailAction.RetryLoad)
                    },
                    onBack = onBack,
                    modifier = modifier
                )
            } else {
                TipDetailReadyState(
                    tip = tip,
                    onAction = onAction,
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
private fun TipDetailLoadingState(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag(TipDetailLoadingStateTestTag),
        contentPadding = PaddingValues(CalmTheme.spacingTokens.screenPadding),
        verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.sectionGap)
    ) {
        items(
            count = TipDetailLoadingPlaceholderCount,
            key = { index -> "tip_detail_loading_$index"}
        ) {
            CalmLoadingPanel()
        }
    }
}

@Composable
private fun TipDetailErrorState(
    message: String,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag(TipDetailErrorStateTestTag),
        contentPadding = PaddingValues(CalmTheme.spacingTokens.screenPadding),
        verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.cardGap)
    ) {
        item {
            CalmErrorPanel(
                title = "Unable to open tip",
                message = message,
                actionLabel = "Try again",
                actionTestTag = TipDetailErrorRetryActionTestTag,
                onActionClick = onRetry
            )
        }

        item {
            CalmErrorPanel(
                title = "Return to journey",
                message = "Go back to the Home journey and choose another tip.",
                actionLabel = "Back to journey",
                actionTestTag = TipDetailErrorBackActionTestTag,
                onActionClick = onBack
            )
        }
    }
}

@Composable
private fun TipDetailReadyState(
    tip: TipDetailUi,
    onAction: (TipDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag(TipDetailReadyStateTestTag),
        contentPadding = PaddingValues(CalmTheme.spacingTokens.screenPadding),
        verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.sectionGap)
    ) {
        item {
            TipDetailMetaBlock(
                tip = tip,
                onToggleBookmark = {
                    onAction(TipDetailAction.ToggleBookmark)
                },
            )
        }

        item {
            TipDetailContentBlock(
                tip = tip
            )
        }

        item {
            TipDetailActionsRow(
                isCompleted = tip.isCompleted,
                onToggleCompleted = {
                    onAction(TipDetailAction.ToggleCompleted)
                },
            )
        }
    }
}

private const val TipDetailLoadingPlaceholderCount = 4
const val TipDetailLoadingStateTestTag = "tip_detail_loading_state"
const val TipDetailErrorStateTestTag = "tip_detail_error_state"
const val TipDetailReadyStateTestTag = "tip_detail_ready_state"
const val TipDetailErrorRetryActionTestTag = "tip_detail_error_retry_action"
const val TipDetailErrorBackActionTestTag = "tip_detail_error_back_action"