package com.example.a30daysofcalmexecution.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        modifier = modifier.fillMaxSize(),
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
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(CalmTheme.spacingTokens.screenPadding),
        verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.cardGap)
    ) {
        item {
            CalmErrorPanel(
                title = "Unable to open tip",
                message = message,
                actionLabel = "Try again",
                onActionClick = onRetry
            )
        }

        item {
            CalmErrorPanel(
                title = "Return to journey",
                message = "Go back to the Home journey and choose another tip.",
                actionLabel = "Back to journey",
                onActionClick = onBack
            )
        }
    }
}

@Composable
private fun TipDetailReadyState(
    tip: TipDetailUi,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(CalmTheme.spacingTokens.screenPadding),
        verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.sectionGap)
    ) {
        item {
            TipDetailMetaBlock(
                tip = tip
            )
        }
    }
}

private const val TipDetailLoadingPlaceholderCount = 4