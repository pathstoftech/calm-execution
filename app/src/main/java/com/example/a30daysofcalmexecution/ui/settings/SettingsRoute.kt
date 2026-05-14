package com.example.a30daysofcalmexecution.ui.settings

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
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmTheme

@Composable
fun SettingsRoute(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsRoutePlaceholder(
        state = uiState,
        onBack = onBack,
        modifier = modifier,
    )
}

@Composable
private fun SettingsRoutePlaceholder(
    state: SettingsUiState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = CalmTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(CalmTheme.spacingTokens.cardPadding),
            verticalArrangement = Arrangement.spacedBy(
                space = CalmTheme.spacingTokens.inlineGap,
                alignment = Alignment.CenterVertically,
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = state.screenTitle,
                style = CalmTheme.typography.headlineSmall,
            )
            Text(
                text = "Settings route connected.",
                style = CalmTheme.typography.bodyMedium,
            )
            Button(onClick = onBack) {
                Text("Back")
            }
        }
    }
}