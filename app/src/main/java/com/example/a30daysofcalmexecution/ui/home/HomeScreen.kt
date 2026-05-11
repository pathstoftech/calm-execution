package com.example.a30daysofcalmexecution.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmLoadingPanel
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmTheme
import com.example.a30daysofcalmexecution.core.ui.AsyncStatus

@Composable
fun HomeScreen(
    state: HomeUiState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier
) {
    when (state.status) {
        AsyncStatus.IDLE,
        AsyncStatus.LOADING -> {
            HomeLoadingState(
                modifier = modifier
            )
        }

        AsyncStatus.ERROR -> {
            HomeErrorStatePlaceHolder(
                message = state.message?.text ?: "Unable to load journey content.",
                modifier = modifier
            )
        }

        AsyncStatus.READY -> {
            HomeContent(
                state = state,
                onAction = onAction,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun HomeLoadingState(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(CalmTheme.spacingTokens.screenPadding),
        verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.cardGap)
    ) {
        item { CalmLoadingPanel() }

        item { CalmLoadingPanel() }

        item { CalmLoadingPanel() }
    }
}

@Composable
private fun HomeErrorStatePlaceHolder(
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(CalmTheme.spacingTokens.screenPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = CalmTheme.typographyTokens.cardBody,
            color = CalmTheme.colorTokens.onScreenBackground
        )
    }
}

@Composable
private fun HomeContent(
    state: HomeUiState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(CalmTheme.spacingTokens.screenPadding),
        verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.sectionGap)
    ) {
        item {
            HomeIntroBlock(
                introText = state.introText
            )
        }

        item {
            JourneyProgressStrip(
                progress = state.journey
            )
        }

        item {
            SectionChipRow(
                tabs = state.sectionTabs,
                onSelectSection = { section ->
                    onAction(HomeAction.SelectSection(section))
                }
            )
        }

        tipSectionFeed(
            sections = state.feedSections,
            onOpenTip = { tipId ->
                onAction(HomeAction.OpenTip(tipId))
            }
        )
    }
}