package com.example.a30daysofcalmexecution.core.data.preferences

import com.example.a30daysofcalmexecution.core.model.SectionKey
import com.example.a30daysofcalmexecution.core.model.ThemeMode
import com.example.a30daysofcalmexecution.core.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    fun observePreferences(): Flow<UserPreferences>

    suspend fun setThemeMode(themeMode: ThemeMode)

    suspend fun setDynamicColorEnabled(enabled: Boolean)

    suspend fun setReducedMotionEnabled(enabled: Boolean)

    suspend fun setLastSelectedSection(sectionKey: SectionKey?)

    suspend fun setHasSeenIntro(seen: Boolean)
}