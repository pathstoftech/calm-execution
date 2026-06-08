package com.pathstoftech.calmexecution.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import com.pathstoftech.calmexecution.core.designsystem.component.CalmErrorPanel
import com.pathstoftech.calmexecution.core.designsystem.component.CalmLabel
import com.pathstoftech.calmexecution.core.designsystem.component.CalmLabelTone
import com.pathstoftech.calmexecution.core.designsystem.component.CalmLoadingPanel
import com.pathstoftech.calmexecution.core.designsystem.theme.CalmTheme
import com.pathstoftech.calmexecution.core.model.ThemeMode
import com.pathstoftech.calmexecution.core.ui.AsyncStatus

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
                onAction = onAction,
                onBack = onBack,
                modifier = modifier,
            )
        }
    }

    if (state.showResetProgressDialog) {
        ResetProgressConfirmationDialog(
            onDismiss = {
                onAction(SettingsAction.DismissResetProgressDialog)
            },
            onConfirm = {
                onAction(SettingsAction.ConfirmResetProgress)
            },
        )
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
    onAction: (SettingsAction) -> Unit,
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
            ThemeModePreferenceRow(
                selectedThemeMode = state.themeMode,
                onThemeModeSelected = { themeMode ->
                    onAction(SettingsAction.SetThemeMode(themeMode))
                },
            )
        }

        item {
            DynamicColorPreferenceRow(
                dynamicColorEnabled = state.dynamicColorEnabled,
                onDynamicColorEnabledChange = { enabled ->
                    onAction(SettingsAction.SetDynamicColorEnabled(enabled))
                },
            )
        }

        item {
            ReducedMotionPreferenceRow(
                reducedMotionEnabled = state.reducedMotionEnabled,
                onReducedMotionEnabledChange = { enabled ->
                    onAction(SettingsAction.SetReducedMotionEnabled(enabled))
                },
            )
        }

        item {
            ResetProgressPreferenceRow(
                onResetProgressClick = {
                    onAction(SettingsAction.ShowResetProgressDialog)
                },
            )
        }

        item {
            AboutPrivacySection()
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
private fun ThemeModePreferenceRow(
    selectedThemeMode: ThemeMode,
    onThemeModeSelected: (ThemeMode) -> Unit,
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(CalmTheme.spacingTokens.cardPadding)
                .selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap),
        ) {
            Text(
                text = "Theme mode",
                style = CalmTheme.typographyTokens.cardTitle,
                color = CalmTheme.colorTokens.onCardContainer,
            )

            Text(
                text = "Choose how the app follows light and dark appearance.",
                style = CalmTheme.typographyTokens.cardBody,
                color = CalmTheme.colorTokens.onCardContainerVariant,
            )

            ThemeMode.entries.forEach { themeMode ->
                ThemeModeOptionRow(
                    themeMode = themeMode,
                    selected = themeMode == selectedThemeMode,
                    onClick = { onThemeModeSelected(themeMode) },
                )
            }
        }
    }
}

@Composable
private fun DynamicColorPreferenceRow(
    dynamicColorEnabled: Boolean,
    onDynamicColorEnabledChange: (Boolean) -> Unit,
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(CalmTheme.spacingTokens.cardPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap),
            ) {
                Text(
                    text = "Dynamic color",
                    style = CalmTheme.typographyTokens.cardTitle,
                    color = CalmTheme.colorTokens.onCardContainer,
                )
                Text(
                    text = "Use colors derived from the device wallpaper when supported.",
                    style = CalmTheme.typographyTokens.cardBody,
                    color = CalmTheme.colorTokens.onCardContainerVariant,
                )
            }

            Switch(
                checked = dynamicColorEnabled,
                onCheckedChange = onDynamicColorEnabledChange,
            )
        }
    }
}

@Composable
private fun ReducedMotionPreferenceRow(
    reducedMotionEnabled: Boolean,
    onReducedMotionEnabledChange: (Boolean) -> Unit,
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(CalmTheme.spacingTokens.cardPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap),
            ) {
                Text(
                    text = "Reduced motion",
                    style = CalmTheme.typographyTokens.cardTitle,
                    color = CalmTheme.colorTokens.onCardContainer,
                )
                Text(
                    text = "Use calmer transitions and avoid unnecessary motion where supported.",
                    style = CalmTheme.typographyTokens.cardBody,
                    color = CalmTheme.colorTokens.onCardContainerVariant,
                )
            }

            Switch(
                checked = reducedMotionEnabled,
                onCheckedChange = onReducedMotionEnabledChange,
            )
        }
    }
}

@Composable
private fun ResetProgressPreferenceRow(
    onResetProgressClick: () -> Unit,
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(CalmTheme.spacingTokens.cardPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap),
            ) {
                Text(
                    text = "Reset progress",
                    style = CalmTheme.typographyTokens.cardTitle,
                    color = CalmTheme.colorTokens.onCardContainer,
                )
                Text(
                    text = "Clear journey progress after confirmation.",
                    style = CalmTheme.typographyTokens.cardBody,
                    color = CalmTheme.colorTokens.onCardContainerVariant,
                )
            }

            OutlinedButton(
                onClick = onResetProgressClick,
            ) {
                Text("Reset")
            }
        }
    }
}

@Composable
private fun AboutPrivacySection(
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(CalmTheme.spacingTokens.cardPadding),
            verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap),
        ) {
            Text(
                text = "About & privacy",
                style = CalmTheme.typographyTokens.cardTitle,
                color = CalmTheme.colorTokens.onCardContainer,
            )

            Text(
                text = "30 Days of Calm Execution stores journey progress and preferences locally on this device.",
                style = CalmTheme.typographyTokens.cardBody,
                color = CalmTheme.colorTokens.onCardContainerVariant,
            )

            Text(
                text = "No account, backend sync, analytics SDK, telemetry SDK, advertising SDK, or runtime crash-reporting SDK is integrated in this build.",
                style = CalmTheme.typographyTokens.cardBody,
                color = CalmTheme.colorTokens.onCardContainerVariant,
            )

            Text(
                text = "Support and privacy contact details are not finalized yet. They must be added before public-store distribution.",
                style = CalmTheme.typographyTokens.cardBody,
                color = CalmTheme.colorTokens.onCardContainerVariant,
            )
        }
    }
}

@Composable
private fun ThemeModeOptionRow(
    themeMode: ThemeMode,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton,
            )
            .padding(vertical = CalmTheme.spacingTokens.small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.inlineGap),
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(CalmTheme.spacingTokens.extraSmall),
        ) {
            Text(
                text = themeMode.label(),
                style = CalmTheme.typographyTokens.cardBody,
                color = CalmTheme.colorTokens.onCardContainer,
            )
            Text(
                text = themeMode.description(),
                style = CalmTheme.typographyTokens.cardSupportingText,
                color = CalmTheme.colorTokens.onCardContainerVariant,
            )
        }
    }
}

@Composable
private fun ResetProgressConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Reset progress?",
                style = CalmTheme.typographyTokens.cardTitle,
            )
        },
        text = {
            Text(
                text = "This will clear your journey progress. The 30-day content and app preferences will stay unchanged.",
                style = CalmTheme.typographyTokens.cardBody,
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
            ) {
                Text("Reset")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text("Cancel")
            }
        },
    )
}

private fun ThemeMode.label(): String =
    when (this) {
        ThemeMode.SYSTEM -> "System"
        ThemeMode.LIGHT -> "Light"
        ThemeMode.DARK -> "Dark"
    }

private fun ThemeMode.description(): String =
    when (this) {
        ThemeMode.SYSTEM -> "Follow device appearance"
        ThemeMode.LIGHT -> "Use light appearance"
        ThemeMode.DARK -> "Use dark appearance"
    }

private const val SettingsLoadingPlaceholderCount = 3