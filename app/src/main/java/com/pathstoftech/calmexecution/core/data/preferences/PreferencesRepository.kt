package com.pathstoftech.calmexecution.core.data.preferences

import com.pathstoftech.calmexecution.core.model.SectionKey
import com.pathstoftech.calmexecution.core.model.ThemeMode
import com.pathstoftech.calmexecution.core.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    fun observePreferences(): Flow<UserPreferences>

    suspend fun setThemeMode(themeMode: ThemeMode)

    suspend fun setDynamicColorEnabled(enabled: Boolean)

    suspend fun setReducedMotionEnabled(enabled: Boolean)

    suspend fun setLastSelectedSection(sectionKey: SectionKey?)

    suspend fun setHasSeenIntro(seen: Boolean)
}