package com.example.a30daysofcalmexecution.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmErrorPanel
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmLoadingPanel
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmLoadingPlaceholder
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
            HomeErrorState(
                message = state.message?.text ?: "Unable to load journey content.",
                onRetry = {
                    onAction(HomeAction.RetryLoad)
                },
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
        item { HomeIntroLoadingPlaceholder() }

        item { JourneyProgressLoadingPlaceholder() }

        item { SectionChipRowLoadingPlaceholder() }

        items(
            count = HomeLoadingFeedPlaceholderCount,
            key = { index -> "home_loading_card_$index" }
        ) {
            CalmLoadingPanel()
        }
    }
}

@Composable
private fun HomeIntroLoadingPlaceholder(
    modifier: Modifier = Modifier
) {
    CalmLoadingPanel(
        modifier = modifier
    )
}

@Composable
private fun JourneyProgressLoadingPlaceholder(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap)
    ) {
        CalmLoadingPlaceholder(
            modifier = Modifier
                .width(CalmTheme.spacingTokens.extraLarge * HomeLoadingShortLineMultiplier)
                .height(CalmTheme.spacingTokens.large)
        )

        CalmLoadingPlaceholder(
            modifier = Modifier
                .fillMaxWidth()
                .height(CalmTheme.spacingTokens.medium)
        )

        CalmLoadingPlaceholder(
            modifier = Modifier
                .width(CalmTheme.spacingTokens.extraLarge * HomeLoadingMediumLineMultiplier)
                .height(CalmTheme.spacingTokens.small)
        )
    }
}

@Composable
private fun SectionChipRowLoadingPlaceholder(
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap),
        verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap)
    ) {
        repeat(HomeLoadingChipPlaceholderCount) { index ->
            CalmLoadingPlaceholder(
                modifier = Modifier
                    .width(
                        if (index == 0) {
                            CalmTheme.spacingTokens.extraLarge * HomeLoadingAllChipMultiplier
                        } else {
                            CalmTheme.spacingTokens.extraLarge * HomeLoadingChipMultiplier
                        }
                    )
                    .height(CalmTheme.spacingTokens.extraLarge)
            )
        }
    }
}

@Composable
private fun HomeErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(CalmTheme.spacingTokens.screenPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CalmErrorPanel(
            title = "Unable to load Home",
            message = message,
            actionLabel = "Try again",
            onActionClick = onRetry
        )
    }
}

@Composable
private fun HomeEmptyFilteredState(
    title: String,
    message: String,
    actionLabel: String,
    onActionClick: () -> Unit,
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
                text = message,
                style = CalmTheme.typographyTokens.cardBody,
                color = CalmTheme.colorTokens.onCardContainerVariant
            )

            TextButton(
                onClick = onActionClick
            ) {
                Text(actionLabel)
            }
        }
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
                bookmarkedOnly = state.bookmarkedOnly,
                onSelectSection = { section ->
                    onAction(HomeAction.SelectSection(section))
                },
                onSetBookmarkedFilter = { enabled ->
                    onAction(HomeAction.SetBookmarkedFilter(enabled))
                }
            )
        }

        val hasVisibleTips = state.feedSections.any { section ->
            section.items.isNotEmpty()
        }

        val shouldShowEmptyFilteredState =
            (state.selectedSection != null || state.bookmarkedOnly) && !hasVisibleTips

        if (shouldShowEmptyFilteredState) {
            item {
                if (state.bookmarkedOnly) {
                    HomeEmptyFilteredState(
                        title = "No bookmarked tips yet",
                        message = "Bookmark tips to return to them quickly during the 30-day journey.",
                        actionLabel = "Show all tips",
                        onActionClick = {
                            onAction(HomeAction.SetBookmarkedFilter(false))
                            onAction(HomeAction.SelectSection(null))
                        }
                    )
                } else {
                    HomeEmptyFilteredState(
                        title = "No tips in this section",
                        message = "Try another phase or return to the full 30-day journey.",
                        actionLabel = "Show all",
                        onActionClick = {
                            onAction(HomeAction.SelectSection(null))
                        }
                    )
                }
            }
        } else {
            tipSectionFeed(
                sections = state.feedSections,
                selectedTipId = state.selectedTipDetail?.id ?: state.selectedTipId,
                onOpenTip = { tipId ->
                    onAction(HomeAction.OpenTip(tipId))
                },
                onToggleBookmark = { tipId ->
                    onAction(HomeAction.ToggleBookmark(tipId))
                },
                onToggleCompleted = { tipId ->
                    onAction(HomeAction.ToggleCompleted(tipId))
                },
            )
        }
    }
}

private const val HomeLoadingFeedPlaceholderCount = 4
private const val HomeLoadingChipPlaceholderCount = 5
private const val HomeLoadingShortLineMultiplier = 5
private const val HomeLoadingMediumLineMultiplier = 7
private const val HomeLoadingAllChipMultiplier = 2
private const val HomeLoadingChipMultiplier = 4