package com.pathstoftech.calmexecution.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pathstoftech.calmexecution.core.model.TipId

@Composable
fun HomeRoute(
    onOpenTip: (TipId) -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
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