package com.example.a30daysofcalmexecution.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmTheme

@Composable
fun CalmExecutionNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        modifier = modifier
    ) {
        composable<HomeRoute> {
            HomeDestinationPlaceholder(
                onOpenTip = {
                    navController.navigate(TipDetailRoute(tipId = "day_01_define_real_priority"))
                },
                onOpenSettings = {
                    navController.navigate(SettingsRoute)
                }
            )
        }

        composable <TipDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<TipDetailRoute>()

            TipDetailDestinationPlaceholder(
                tipId = route.tipId,
                onBack = navController::popBackStack,
            )
        }

        composable<SettingsRoute> {
            SettingsDestinationPlaceholder(
                onBack = navController::popBackStack
            )
        }
    }
}

@Composable
private fun HomeDestinationPlaceholder(
    onOpenTip: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    PlaceholderDestinationSurface(modifier = modifier) {
        Text(
            text = "Home",
            style = CalmTheme.typography.headlineSmall
        )

        Text(
            text = "Navigation host is active.",
            style = CalmTheme.typography.bodyMedium
        )

        Button(onClick = onOpenTip) {
            Text("One sample tip")
        }

        Button(onClick = onOpenSettings) {
            Text("Open settings")
        }
    }
}

@Composable
private fun TipDetailDestinationPlaceholder(
    tipId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    PlaceholderDestinationSurface(modifier = modifier) {
        Text(
            text = "Tip detail",
            style = CalmTheme.typography.headlineSmall
        )

        Text(
            text = tipId,
            style = CalmTheme.typography.bodyMedium
        )

        Button(onClick = onBack) {
            Text("Back")
        }
    }
}

@Composable
private fun SettingsDestinationPlaceholder(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    PlaceholderDestinationSurface(modifier = modifier) {
        Text(
            text = "Settings",
            style = CalmTheme.typography.headlineSmall
        )

        Text(
            text = "Settings route placeholder.",
            style = CalmTheme.typography.bodyMedium
        )

        Button(onClick = onBack) {
            Text("Back")
        }
    }
}

@Composable
private fun PlaceholderDestinationSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = CalmTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(CalmTheme.spacingTokens.cardPadding),
            verticalArrangement = Arrangement.spacedBy(
                space = CalmTheme.spacingTokens.inlineGap,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }
}