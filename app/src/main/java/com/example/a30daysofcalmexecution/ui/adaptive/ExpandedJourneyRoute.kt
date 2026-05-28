package com.example.a30daysofcalmexecution.ui.adaptive

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.focused
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmTheme
import com.example.a30daysofcalmexecution.core.ui.AsyncStatus
import com.example.a30daysofcalmexecution.ui.detail.TipDetailAction
import com.example.a30daysofcalmexecution.ui.detail.TipDetailScreen
import com.example.a30daysofcalmexecution.ui.detail.TipDetailUiState
import com.example.a30daysofcalmexecution.ui.home.HomeAction
import com.example.a30daysofcalmexecution.ui.home.HomeScreen
import com.example.a30daysofcalmexecution.ui.home.HomeUiState
import com.example.a30daysofcalmexecution.ui.home.HomeViewModel

@Composable
fun ExpandedJourneyRoute(
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler(
        enabled = uiState.selectedTipDetail != null,
    ) {
        viewModel.onAction(HomeAction.SelectExpandedDetail(null))
    }

    ExpandedListDetailLayout(
        modifier = modifier.testTag(ExpandedJourneyRouteTestTag),
        listPane = {
            val detailSelected = uiState.selectedTipDetail != null

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (detailSelected) {
                            Modifier.clearAndSetSemantics { }
                        } else {
                            Modifier
                        },
                    ),
            ) {
                HomeScreen(
                    state = uiState,
                    onAction = { action ->
                        when (action) {
                            is HomeAction.OpenTip -> {
                                viewModel.onAction(
                                    HomeAction.SelectExpandedDetail(action.tipId),
                                )
                            }

                            HomeAction.OpenSettings -> {
                                onOpenSettings()
                            }

                            else -> {
                                viewModel.onAction(action)
                            }
                        }
                    },
                )
            }
        },
        detailPane = {
            ExpandedSelectedDetailPane(
                state = uiState,
                onHomeAction = viewModel::onAction,
                onToggleSelectedDetailCompleted = viewModel::toggleSelectedExpandedDetailCompleted,
            )
        },
    )
}

@Composable
private fun ExpandedSelectedDetailPane(
    state: HomeUiState,
    onHomeAction: (HomeAction) -> Unit,
    onToggleSelectedDetailCompleted: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedTip = state.selectedTipDetail

    if (selectedTip == null) {
        ExpandedEmptyDetailPlaceholder(
            modifier = modifier,
        )
        return
    }

    var shouldMoveAccessibilityFocus by remember(selectedTip.id) {
        mutableStateOf(false)
    }

    LaunchedEffect(selectedTip.id) {
        shouldMoveAccessibilityFocus = false
        withFrameNanos { }
        shouldMoveAccessibilityFocus = true
        withFrameNanos { }
        shouldMoveAccessibilityFocus = false
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .semantics {
                paneTitle = "Tip detail"
                liveRegion = LiveRegionMode.Polite
            }
            .testTag(ExpandedSelectedDetailFocusTargetTestTag),
    ) {
        Box(
            modifier = Modifier
                .size(1.dp)
                .semantics {
                    contentDescription =
                        "Opened ${selectedTip.dayLabel}: ${selectedTip.title}"

                    if (shouldMoveAccessibilityFocus) {
                        focused = true
                    }
                },
        )

        ExpandedDetailReturnAction(
            onClick = {
                onHomeAction(HomeAction.SelectExpandedDetail(null))
            },
            modifier = Modifier.align(Alignment.Start),
        )

        TipDetailScreen(
            state = TipDetailUiState(
                status = AsyncStatus.READY,
                screenTitle = selectedTip.title,
                tip = selectedTip,
                message = state.message,
            ),
            onAction = { action ->
                when (action) {
                    TipDetailAction.ToggleBookmark -> {
                        onHomeAction(HomeAction.ToggleBookmark(selectedTip.id))
                    }

                    TipDetailAction.ToggleCompleted -> {
                        onToggleSelectedDetailCompleted()
                    }

                    TipDetailAction.RetryLoad -> {
                        onHomeAction(HomeAction.RetryLoad)
                    }

                    TipDetailAction.DismissMessage -> {
                        onHomeAction(HomeAction.DismissMessage)
                    }
                }
            },
            onBack = {
                onHomeAction(HomeAction.SelectExpandedDetail(null))
            },
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
        )
    }
}

@Composable
private fun ExpandedDetailReturnAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .testTag(ExpandedDetailReturnActionTestTag)
            .semantics {
                contentDescription = "Back to journey feed"
            }
            .clickable(
                role = Role.Button,
                onClick = onClick,
            ),
        shape = CalmTheme.shapeTokens.cardContainerLarge,
        color = CalmTheme.colorTokens.cardContainer,
        contentColor = CalmTheme.colorTokens.onCardContainer,
        tonalElevation = CalmTheme.elevationTokens.cardResting,
        shadowElevation = CalmTheme.elevationTokens.none,
        border = BorderStroke(
            width = 1.dp,
            color = CalmTheme.colorTokens.onCardContainerVariant,
        ),
    ) {
        Row(
            modifier = Modifier
                .heightIn(min = 40.dp)
                .padding(
                    horizontal = 12.dp,
                    vertical = 6.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Close Tip",
                style = CalmTheme.typographyTokens.actionLabel,
                color = CalmTheme.colorTokens.onCardContainer,
            )
        }
    }
}

const val ExpandedJourneyRouteTestTag = "expanded_journey_route"
const val ExpandedSelectedDetailFocusTargetTestTag = "expanded_selected_detail_focus_target"
const val ExpandedDetailReturnActionTestTag = "expanded_detail_return_action"