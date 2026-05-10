package com.example.a30daysofcalmexecution.ui.home

import android.view.Surface
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
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
            HomeContentPlaceHolder(
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
private fun HomeContentPlaceHolder(
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
            HomeStructurePlaceholderCard(
                title = "Home content ready",
                body = "Intro block, journey strip, phase chips, and grouped feed are wired in the next Home tasks."
            )
        }

        item {
            HomeStructurePlaceholderCard(
                title = "Journey state connected",
                body = "${state.journey.completedCount} of ${state.journey.totalCount} days completed."
            )
        }

        item {
            HomeStructurePlaceholderCard(
                title = "Catalog structure connected",
                body = "${state.sectionTabs.size} phases and ${state.feedSections.sumOf { section -> section.items.size }} tips are ready for rendering."
            )
        }
    }
}

@Composable
private fun HomeStructurePlaceholderCard(
    title: String,
    body: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = CalmTheme.shapeTokens.cardContainerLarge,
        color = CalmTheme.colorTokens.cardContainer,
        contentColor = CalmTheme.colorTokens.onCardContainer,
        tonalElevation = CalmTheme.elevationTokens.cardResting,
        shadowElevation = CalmTheme.elevationTokens.none
    ) {
        Column(
            modifier = Modifier.padding(CalmTheme.spacingTokens.cardPadding),
            verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap)
        ) {
            Text(
                text = title,
                style = CalmTheme.typographyTokens.cardTitle,
                color = CalmTheme.colorTokens.onCardContainer
            )

            Text(
                text = body,
                style = CalmTheme.typographyTokens.cardBody,
                color = CalmTheme.colorTokens.cardContainerVariant
            )
        }
    }
}