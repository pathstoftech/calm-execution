package com.pathstoftech.calmexecution.navigation

import kotlinx.serialization.Serializable

sealed interface AppRoute {
}

@Serializable
data object HomeRoute: AppRoute

@Serializable
data class TipDetailRoute(
    val tipId: String
) : AppRoute {
    init {
        require(tipId.isNotBlank()) {
            "tipId must not be blank."
        }
    }
}

@Serializable
data object SettingsRoute : AppRoute