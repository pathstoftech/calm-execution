package com.example.a30daysofcalmexecution.ui.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TipDetailRoute(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TipDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TipDetailScreen(
        state = uiState,
        onAction = viewModel::onAction,
        onBack = onBack,
        modifier = modifier
    )
}