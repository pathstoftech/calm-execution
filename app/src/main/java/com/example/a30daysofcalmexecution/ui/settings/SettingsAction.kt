package com.example.a30daysofcalmexecution.ui.settings

import androidx.compose.runtime.Immutable
import com.example.a30daysofcalmexecution.core.model.ThemeMode

@Immutable
sealed interface SettingsAction {
    data class SetThemeMode(
        val themeMode: ThemeMode
    ) : SettingsAction

    data class SetDynamicColorEnabled(
        val enabled: Boolean
    ) : SettingsAction

    data class SetReducedMotionEnabled(
        val enabled: Boolean
    ) : SettingsAction

    data object ShowResetProgressDialog : SettingsAction

    data object DismissResetProgressDialog : SettingsAction

    data object ConfirmResetProgress : SettingsAction

    data object RetryLoad : SettingsAction

    data object DismissMessage : SettingsAction
}