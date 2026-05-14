package com.example.a30daysofcalmexecution.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmErrorPanel
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmLabel
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmLabelTone
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmLoadingPanel
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmTheme
import com.example.a30daysofcalmexecution.core.model.ThemeMode
import com.example.a30daysofcalmexecution.core.ui.AsyncStatus

@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onAction: (SettingsAction) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state.status) {
        AsyncStatus.IDLE,
        AsyncStatus.LOADING -> {
            SettingsLoadingState(
                modifier = modifier,
            )
        }

        AsyncStatus.ERROR -> {
            SettingsErrorState(
                message = state.message?.text ?: "Unable to load settings.",
                onRetry = { onAction(SettingsAction.RetryLoad) },
                onBack = onBack,
                modifier = modifier,
            )
        }

        AsyncStatus.READY -> {
            SettingsReadyState(
                state = state,
                onBack = onBack,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun SettingsLoadingState(
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("settings_loading_state"),
        contentPadding = PaddingValues(CalmTheme.spacingTokens.screenPadding),
        verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.cardGap),
    ) {
        items(
            count = SettingsLoadingPlaceholderCount,
            key = { index -> "settings_loading_$index" },
        ) {
            CalmLoadingPanel()
        }
    }
}

@Composable
private fun SettingsErrorState(
    message: String,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("settings_error_state"),
        contentPadding = PaddingValues(CalmTheme.spacingTokens.screenPadding),
        verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.cardGap),
    ) {
        item {
            CalmErrorPanel(
                title = "Unable to load Settings",
                message = message,
                actionLabel = "Try again",
                onActionClick = onRetry,
            )
        }

        item {
            CalmErrorPanel(
                title = "Return to journey",
                message = "Go back to the Home journey and try Settings again later.",
                actionLabel = "Back",
                onActionClick = onBack,
            )
        }
    }
}

@Composable
private fun SettingsReadyState(
    state: SettingsUiState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("settings_ready_state"),
        contentPadding = PaddingValues(CalmTheme.spacingTokens.screenPadding),
        verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.sectionGap),
    ) {
        item {
            SettingsHeader(
                title = state.screenTitle,
            )
        }

        item {
            SettingsSummaryCard(
                themeMode = state.themeMode,
                dynamicColorEnabled = state.dynamicColorEnabled,
                reducedMotionEnabled = state.reducedMotionEnabled,
            )
        }

        item {
            SettingsFutureControlsCard()
        }

        item {
            OutlinedButton(
                onClick = onBack,
            ) {
                Text("Back")
            }
        }
    }
}

@Composable
private fun SettingsHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = CalmTheme.shapeTokens.cardContainerLarge,
        color = CalmTheme.colorTokens.cardContainer,
        contentColor = CalmTheme.colorTokens.onCardContainer,
        tonalElevation = CalmTheme.elevationTokens.cardResting,
        shadowElevation = CalmTheme.elevationTokens.none,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(CalmTheme.spacingTokens.cardPadding),
            verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap),
        ) {
            CalmLabel(
                text = "Preferences",
                tone = CalmLabelTone.Primary,
            )
            Text(
                text = title,
                style = CalmTheme.typography.headlineSmall,
                color = CalmTheme.colorTokens.onCardContainer,
            )
            Text(
                text = "Control local app preferences without changing the 30-day content journey.",
                style = CalmTheme.typographyTokens.cardBody,
                color = CalmTheme.colorTokens.onCardContainerVariant,
            )
        }
    }
}

@Composable
private fun SettingsSummaryCard(
    themeMode: ThemeMode,
    dynamicColorEnabled: Boolean,
    reducedMotionEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = CalmTheme.shapeTokens.cardContainerLarge,
        color = CalmTheme.colorTokens.cardContainer,
        contentColor = CalmTheme.colorTokens.onCardContainer,
        tonalElevation = CalmTheme.elevationTokens.cardResting,
        shadowElevation = CalmTheme.elevationTokens.none,
    ) {
        Column(
            modifier = Modifier.padding(CalmTheme.spacingTokens.cardPadding),
            verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap),
        ) {
            Text(
                text = "Current preferences",
                style = CalmTheme.typographyTokens.cardTitle,
                color = CalmTheme.colorTokens.onCardContainer,
            )
            Text(
                text = "Theme: ${themeMode.label()}",
                style = CalmTheme.typographyTokens.cardBody,
                color = CalmTheme.colorTokens.onCardContainerVariant,
            )
            Text(
                text = "Dynamic color: ${dynamicColorEnabled.enabledLabel()}",
                style = CalmTheme.typographyTokens.cardBody,
                color = CalmTheme.colorTokens.onCardContainerVariant,
            )
            Text(
                text = "Reduced motion: ${reducedMotionEnabled.enabledLabel()}",
                style = CalmTheme.typographyTokens.cardBody,
                color = CalmTheme.colorTokens.onCardContainerVariant,
            )
        }
    }
}

@Composable
private fun SettingsFutureControlsCard(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = CalmTheme.shapeTokens.cardContainerLarge,
        color = CalmTheme.colorTokens.cardContainerVariant,
        contentColor = CalmTheme.colorTokens.onCardContainerVariant,
        tonalElevation = CalmTheme.elevationTokens.none,
        shadowElevation = CalmTheme.elevationTokens.none,
    ) {
        Column(
            modifier = Modifier.padding(CalmTheme.spacingTokens.cardPadding),
            verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap),
        ) {
            CalmLabel(
                text = "Next",
                tone = CalmLabelTone.Neutral,
            )
            Text(
                text = "Preference controls will be added in the next Settings tasks.",
                style = CalmTheme.typographyTokens.cardTitle,
                color = CalmTheme.colorTokens.onCardContainerVariant,
            )
            Text(
                text = "Theme mode, dynamic color, reduced motion, and reset progress stay separated into their own backlog items.",
                style = CalmTheme.typographyTokens.cardBody,
                color = CalmTheme.colorTokens.onCardContainerVariant,
            )
        }
    }
}

private fun ThemeMode.label(): String =
    when (this) {
        ThemeMode.SYSTEM -> "System"
        ThemeMode.LIGHT -> "Light"
        ThemeMode.DARK -> "Dark"
    }

private fun Boolean.enabledLabel(): String =
    if (this) {
        "Enabled"
    } else {
        "Disabled"
    }

private const val SettingsLoadingPlaceholderCount = 3