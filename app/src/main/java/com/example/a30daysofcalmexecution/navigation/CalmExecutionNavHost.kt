package com.example.a30daysofcalmexecution.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmErrorPanel
import com.example.a30daysofcalmexecution.core.designsystem.component.CalmLoadingPanel
import com.example.a30daysofcalmexecution.core.designsystem.theme.CalmTheme

@Composable
fun CalmExecutionNavHost(
    navController: NavHostController,
    isKnownTipId: suspend (String) -> Boolean,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        modifier = modifier
    ) {
        homeRoute(
            onOpenSampleTip = {
                navController.navigate(TipDetailRoute(tipId = "day_01_define_real_priority"))
            },
            onOpenSettings = {
                navController.navigate(SettingsRoute)
            },
        )

        tipDetailRoute(
            isKnownTipId = isKnownTipId,
            onBack = navController::popBackStack
        )

        settingsRoute(
            onBack = navController::popBackStack
        )
    }
}

private fun NavGraphBuilder.homeRoute(
    onOpenSampleTip: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    composable<HomeRoute> {
        HomeDestinationPlaceholder(
            onOpenTip = onOpenSampleTip,
            onOpenSettings = onOpenSettings,
        )
    }
}

private fun NavGraphBuilder.tipDetailRoute(
    isKnownTipId: suspend (String) -> Boolean,
    onBack: () -> Unit
) {
    composable<TipDetailRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<TipDetailRoute>()

        TipDetailRoutePlaceholder(
            tipId = route.tipId,
            isKnownTipId = isKnownTipId,
            onBack = onBack
        )
    }
}

private fun NavGraphBuilder.settingsRoute(
    onBack: () -> Unit
) {
    composable<SettingsRoute> {
        SettingsDestinationPlaceholder(
            onBack = onBack
        )
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
private fun TipDetailRoutePlaceholder(
    tipId: String,
    isKnownTipId: suspend (String) -> Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val validationState by produceState<TipDetailRouteValidationState>(
        initialValue = TipDetailRouteValidationState.Loading,
        key1 = tipId
    ) {
        value = runCatching {
            if (isKnownTipId(tipId)) {
                TipDetailRouteValidationState.Valid
            } else {
                TipDetailRouteValidationState.Invalid
            }
        }.getOrElse {
            TipDetailRouteValidationState.Error
        }
    }

    when (validationState) {
        TipDetailRouteValidationState.Loading -> {
            PlaceholderDestinationSurface(modifier = modifier) {
                CalmLoadingPanel()
            }
        }

        TipDetailRouteValidationState.Valid -> {
            TipDetailDestinationPlaceholder(
                tipId = tipId,
                onBack = onBack,
                modifier = modifier
            )
        }

        TipDetailRouteValidationState.Invalid -> {
            InvalidTipIdDestinationPlaceholder(
                onBack = onBack,
                modifier = modifier
            )
        }

        TipDetailRouteValidationState.Error -> {
            InvalidTipIdDestinationPlaceholder(
                title = "Unable to open tip",
                message = "The tip catalog could not be checked right now.",
                onBack = onBack,
                modifier = modifier
            )
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
private fun InvalidTipIdDestinationPlaceholder(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Tip not found",
    message: String = "This tip is not available in the current catalog."
) {
    PlaceholderDestinationSurface(modifier = modifier) {
        CalmErrorPanel(
            title = title,
            message = message,
            actionLabel = "Back to journey",
            onActionClick = onBack
        )
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

private sealed interface TipDetailRouteValidationState {
    data object Loading: TipDetailRouteValidationState
    data object Valid: TipDetailRouteValidationState
    data object Invalid: TipDetailRouteValidationState
    data object Error: TipDetailRouteValidationState
}