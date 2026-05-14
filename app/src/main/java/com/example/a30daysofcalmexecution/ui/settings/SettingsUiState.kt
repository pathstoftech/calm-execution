package com.example.a30daysofcalmexecution.ui.settings

import androidx.compose.runtime.Immutable
import com.example.a30daysofcalmexecution.core.model.ThemeMode
import com.example.a30daysofcalmexecution.core.ui.AsyncStatus
import com.example.a30daysofcalmexecution.core.ui.UiMessage

@Immutable
data class SettingsUiState(
    val status: AsyncStatus = AsyncStatus.LOADING,
    val screenTitle: String = "Settings",
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val dynamicColorEnabled: Boolean = false,
    val reducedMotionEnabled: Boolean = false,
    val showResetProgressDialog: Boolean = false,
    val message: UiMessage? = null
)