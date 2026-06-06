package com.pathstoftech.calmexecution.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pathstoftech.calmexecution.core.model.TipId
import com.pathstoftech.calmexecution.ui.adaptive.ExpandedJourneyRoute
import com.pathstoftech.calmexecution.ui.detail.TipDetailRoute as TipDetailFeatureRoot
import com.pathstoftech.calmexecution.ui.home.HomeRoute as HomeFeatureRoot
import com.pathstoftech.calmexecution.ui.settings.SettingsRoute as SettingsFeatureRoot

@Composable
fun CalmExecutionNavHost(
    navController: NavHostController,
    isKnownTipId: suspend (String) -> Boolean,
    modifier: Modifier = Modifier,
    useExpandedHomeLayout: Boolean = false,
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        modifier = modifier
    ) {
        homeRoute(
            onOpenTip = { tipId ->
                navController.navigate(TipDetailRoute(tipId = tipId.value))
            },
            onOpenSettings = {
                navController.navigate(SettingsRoute) {
                    launchSingleTop = true
                }
            },
            useExpandedHomeLayout = useExpandedHomeLayout,
        )

        tipDetailRoute(
            onBack = navController::popBackStack
        )

        settingsRoute(
            onBack = navController::popBackStack
        )
    }
}

private fun NavGraphBuilder.homeRoute(
    onOpenTip: (TipId) -> Unit,
    onOpenSettings: () -> Unit,
    useExpandedHomeLayout: Boolean,
) {
    composable<HomeRoute> {
        if (useExpandedHomeLayout) {
            ExpandedJourneyRoute(
                onOpenSettings = onOpenSettings,
            )
        } else {
            HomeFeatureRoot(
                onOpenTip = onOpenTip,
                onOpenSettings = onOpenSettings,
            )
        }
    }
}

private fun NavGraphBuilder.tipDetailRoute(
    onBack: () -> Unit
) {
    composable<TipDetailRoute> {
        TipDetailFeatureRoot(
            onBack = onBack
        )
    }
}

private fun NavGraphBuilder.settingsRoute(
    onBack: () -> Unit
) {
    composable<SettingsRoute> {
        SettingsFeatureRoot(
            onBack = onBack
        )
    }
}