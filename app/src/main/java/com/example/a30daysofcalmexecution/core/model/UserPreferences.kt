package com.example.a30daysofcalmexecution.core.model

import java.nio.channels.SelectionKey

data class UserPreferences(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val dynamicColorEnabled: Boolean = false,
    val reducedMotionEnabled: Boolean = false,
    val lastSelectedSectionKey: SectionKey? = null,
    val hasSeenIntro: Boolean = false
)
