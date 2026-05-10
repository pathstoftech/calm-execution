package com.example.a30daysofcalmexecution.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmTheme
import com.example.a30daysofcalmexecution.core.model.TipId

@Composable
fun HomeRoute(
    onOpenTip: (TipId) -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeRoutePlaceholder(
        state = uiState,
        onAction = { action ->
            when (action) {
                is HomeAction.OpenTip -> onOpenTip(action.tipId)

                HomeAction.OpenSettings -> onOpenSettings()

                else -> viewModel.onAction(action)
            }
        },
        modifier = modifier
    )
}

@Composable
private fun HomeRoutePlaceholder(
    state: HomeUiState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(CalmTheme.spacingTokens.cardPadding),
        verticalArrangement = Arrangement.spacedBy(
            space = CalmTheme.spacingTokens.inlineGap,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = state.screenTitle,
            style = CalmTheme.typographyTokens.screenTitle
        )

        Text(
            text = "Home route is connected.",
            style = CalmTheme.typographyTokens.cardBody
        )

        Button(
            onClick = {
                state.featuredTipId?.let { tipId ->
                    onAction(HomeAction.OpenTip(tipId))
                }
            },
            enabled = state.featuredTipId != null
        ) {
            Text("Open featured tip")
        }

        Button(
            onClick = {
                onAction(HomeAction.OpenSettings)
            }
        ) {
            Text("Open settings")
        }
    }
}