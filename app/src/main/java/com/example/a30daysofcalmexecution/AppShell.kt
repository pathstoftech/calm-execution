package com.example.a30daysofcalmexecution

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmTopAppBar
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmTheme
import com.example.a30daysofcalmexecution.navigation.CalmExecutionNavHost

@Composable
fun AppShell(
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
                }
            )
        },
    ) { innerPadding ->
        CalmExecutionNavHost(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
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
    ) {
        Text(
            text = "Back",
            style = CalmTheme.typographyTokens.actionLabel,
            color = CalmTheme.colorTokens.onScreenBackground
        )
    }
}