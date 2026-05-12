package com.example.a30daysofcalmexecution.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmErrorPanel
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmLoadingPanel
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmTheme
import com.example.a30daysofcalmexecution.core.ui.AsyncStatus

@Composable
fun TipDetailRoute(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TipDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TipDetailRouteContent(
        state = uiState,
        onAction = viewModel::onAction,
        onBack = onBack,
        modifier = modifier
    )
}

@Composable
private fun TipDetailRouteContent(
    state: TipDetailUiState,
    onAction: (TipDetailAction) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (state.status) {
        AsyncStatus.IDLE,
        AsyncStatus.LOADING -> {
            TipDetailPlaceholderSurface(modifier = modifier) {
                CalmLoadingPanel()
            }
        }

        AsyncStatus.ERROR -> {
            TipDetailPlaceholderSurface(modifier = modifier) {
                CalmErrorPanel(
                    title = "Unable to open tip",
                    message = state.message?.text ?: "This tip is not available right now.",
                    actionLabel = "Try again",
                    onActionClick = {
                        onAction(TipDetailAction.RetryLoad)
                    }
                )

                Button(onClick = onBack) {
                    Text("Back to journey")
                }
            }
        }

        AsyncStatus.READY -> {
            val tip = state.tip

            if (tip == null) {
                TipDetailPlaceholderSurface(modifier = modifier) {
                    CalmErrorPanel(
                        title = "Unable to open tip",
                        message = "This tip is not available right now.",
                        actionLabel = "Back to journey",
                        onActionClick = onBack,
                    )
                }
            } else {
                TipDetailPlaceholderSurface(modifier = modifier) {
                    Text(
                        text = state.screenTitle,
                        style = CalmTheme.typography.headlineSmall
                    )

                    Text(
                        text = tip.id.value,
                        style = CalmTheme.typography.bodyMedium
                    )

                    Button(onClick = onBack) {
                        Text("Back")
                    }
                }
            }
        }
    }
}

@Composable
private fun TipDetailPlaceholderSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = CalmTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(CalmTheme.spacingTokens.cardPadding),
            verticalArrangement = Arrangement.spacedBy(
                space = CalmTheme.spacingTokens.inlineGap,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }
}