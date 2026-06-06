package com.pathstoftech.calmexecution.ui.settings

import androidx.compose.runtime.Immutable
import com.pathstoftech.calmexecution.core.model.ThemeMode
import com.pathstoftech.calmexecution.core.ui.AsyncStatus
import com.pathstoftech.calmexecution.core.ui.UiMessage

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