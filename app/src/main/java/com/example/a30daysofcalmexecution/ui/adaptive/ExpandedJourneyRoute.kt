package com.example.a30daysofcalmexecution.ui.adaptive

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

    ExpandedListDetailLayout(
        modifier = modifier.testTag(ExpandedJourneyRouteTestTag),
        listPane = {
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
        },
        detailPane = {
            ExpandedSelectedDetailPane(
                state = uiState,
                onHomeAction = viewModel::onAction,
            )
        },
    )
}

@Composable
private fun ExpandedSelectedDetailPane(
    state: HomeUiState,
    onHomeAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedTip = state.selectedTipDetail

    if (selectedTip == null) {
        ExpandedEmptyDetailPlaceholder(
            modifier = modifier,
        )
        return
    }

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
                    onHomeAction(HomeAction.ToggleCompleted(selectedTip.id))
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
        modifier = modifier,
    )
}

const val ExpandedJourneyRouteTestTag = "expanded_journey_route"