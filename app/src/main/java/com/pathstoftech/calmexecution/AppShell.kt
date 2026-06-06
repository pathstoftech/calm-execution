package com.pathstoftech.calmexecution

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pathstoftech.calmexecution.core.designsystem.component.CalmTopAppBar
import com.pathstoftech.calmexecution.core.designsystem.theme.CalmTheme
import com.pathstoftech.calmexecution.navigation.CalmExecutionNavHost
import com.pathstoftech.calmexecution.navigation.SettingsRoute
import com.pathstoftech.calmexecution.ui.adaptive.AdaptiveAppShell
import com.pathstoftech.calmexecution.ui.adaptive.ExpandedAppScaffold

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
                        CalmExecutionNavHost(
                            navController = navController,
                            isKnownTipId = isKnownTipId,
                            modifier = Modifier.fillMaxSize(),
                            useExpandedHomeLayout = true,
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
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.testTag(CompactBackActionTestTag),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_back_24),
            contentDescription = "Navigate back",
            tint = CalmTheme.colorTokens.onScreenBackground,
        )
    }
}

@Composable
fun CompactSettingsAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.testTag(CompactSettingsActionTestTag),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_settings_24),
            contentDescription = "Open settings",
            tint = CalmTheme.colorTokens.onScreenBackground,
        )
    }
}

private const val CompactBackActionTestTag = "compact_back_action"
private const val CompactSettingsActionTestTag = "compact_settings_action"