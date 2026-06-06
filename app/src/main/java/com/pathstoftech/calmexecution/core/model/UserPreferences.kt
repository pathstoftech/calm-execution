package com.pathstoftech.calmexecution.core.model

data class UserPreferences(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val dynamicColorEnabled: Boolean = false,
    val reducedMotionEnabled: Boolean = false,
    val lastSelectedSectionKey: SectionKey? = null,
    val hasSeenIntro: Boolean = false
)
