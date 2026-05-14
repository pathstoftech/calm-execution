package com.example.a30daysofcalmexecution

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmTopAppBar
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmTheme
import com.example.a30daysofcalmexecution.navigation.CalmExecutionNavHost
import com.example.a30daysofcalmexecution.navigation.SettingsRoute
import com.example.a30daysofcalmexecution.ui.adaptive.AdaptiveAppShell
import com.example.a30daysofcalmexecution.ui.adaptive.ExpandedAppScaffold
import com.example.a30daysofcalmexecution.ui.adaptive.ExpandedBlankDetailPane
import com.example.a30daysofcalmexecution.ui.adaptive.ExpandedListDetailLayout

@Composable
fun AppShell(
    isKnownTipId: suspend (String) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val canNavigateBack =
        currentBackStackEntry != null && navController.previousBackStackEntry != null

    Scaffold(
        modifier = modifier,
        containerColor = CalmTheme.colorTokens.screenBackground,
        contentColor = CalmTheme.colorTokens.onScreenBackground,
        topBar = {
            CalmTopAppBar(
                title = "30 Days of Calm Execution",
                navigationIcon = {
                    if (canNavigateBack) {
                        CompactBackAction(
                            onClick = navController::popBackStack
                        )
                    }
                },
                actions = {
                    if (!canNavigateBack) {
                        CompactSettingsAction(
                            onClick = {
                                navController.navigate(SettingsRoute) {
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        AdaptiveAppShell(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            compactContent = {
                CalmExecutionNavHost(
                    navController = navController,
                    isKnownTipId = isKnownTipId,
                    modifier = Modifier.fillMaxSize(),
                )
            },
            expandedContent = {
                ExpandedAppScaffold(
                    content = {
                        ExpandedListDetailLayout(
                            listPane = {
                                CalmExecutionNavHost(
                                    navController = navController,
                                    isKnownTipId = isKnownTipId,
                                    modifier = Modifier.fillMaxSize(),
                                )
                            },
                            detailPane = {
                                ExpandedBlankDetailPane()
                            },
                        )
                    },
                )
            },
        )
    }
}

@Composable
fun CompactBackAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.testTag(CompactBackActionTestTag)
    ) {
        Text(
            text = "Back",
            style = CalmTheme.typographyTokens.actionLabel,
            color = CalmTheme.colorTokens.onScreenBackground
        )
    }
}

@Composable
fun CompactSettingsAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.testTag(CompactSettingsActionTestTag)
    ) {
        Text(
            text = "Settings",
            style = CalmTheme.typographyTokens.actionLabel,
            color = CalmTheme.colorTokens.onScreenBackground
        )
    }
}

private const val CompactBackActionTestTag = "compact_back_action"
private const val CompactSettingsActionTestTag = "compact_settings_action"