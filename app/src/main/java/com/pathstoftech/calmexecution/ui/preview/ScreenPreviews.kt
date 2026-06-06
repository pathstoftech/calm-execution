package com.pathstoftech.calmexecution.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.pathstoftech.calmexecution.ui.detail.TipDetailScreen
import com.pathstoftech.calmexecution.ui.home.HomeScreen
import com.pathstoftech.calmexecution.ui.settings.SettingsScreen

@Preview(
    name = "Home - Ready",
    showBackground = true,
)
@Composable
private fun HomeReadyPreview() {
    CalmExecutionPreviewContent {
        HomeScreen(
            state = PreviewUiStates.HomeReady,
            onAction = {},
        )
    }
}

@Preview(
    name = "Home - Empty Filtered",
    showBackground = true,
)
@Composable
private fun HomeEmptyFilteredPreview() {
    CalmExecutionPreviewContent {
        HomeScreen(
            state = PreviewUiStates.HomeEmptyFiltered,
            onAction = {},
        )
    }
}

@Preview(
    name = "Detail - Ready",
    showBackground = true,
)
@Composable
private fun DetailReadyPreview() {
    CalmExecutionPreviewContent {
        TipDetailScreen(
            state = PreviewUiStates.DetailReady,
            onAction = {},
            onBack = {},
        )
    }
}

@Preview(
    name = "Detail - Error",
    showBackground = true,
)
@Composable
private fun DetailErrorPreview() {
    CalmExecutionPreviewContent {
        TipDetailScreen(
            state = PreviewUiStates.DetailError,
            onAction = {},
            onBack = {},
        )
    }
}

@Preview(
    name = "Settings - Ready",
    showBackground = true,
)
@Composable
private fun SettingsReadyPreview() {
    CalmExecutionPreviewContent {
        SettingsScreen(
            state = PreviewUiStates.SettingsReady,
            onAction = {},
            onBack = {},
        )
    }
}

@Preview(
    name = "Settings - Reset Dialog",
    showBackground = true,
)
@Composable
private fun SettingsResetDialogPreview() {
    CalmExecutionPreviewContent {
        SettingsScreen(
            state = PreviewUiStates.SettingsResetDialog,
            onAction = {},
            onBack = {},
        )
    }
}