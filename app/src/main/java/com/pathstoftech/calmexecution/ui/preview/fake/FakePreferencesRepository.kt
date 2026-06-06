package com.pathstoftech.calmexecution.ui.preview.fake

import com.pathstoftech.calmexecution.core.data.preferences.PreferencesRepository
import com.pathstoftech.calmexecution.core.model.SectionKey
import com.pathstoftech.calmexecution.core.model.ThemeMode
import com.pathstoftech.calmexecution.core.model.UserPreferences
import com.pathstoftech.calmexecution.ui.preview.PreviewData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakePreferencesRepository(
    initialPreferences: UserPreferences = PreviewData.DefaultPreferences,
) : PreferencesRepository {

    private val preferences = MutableStateFlow(initialPreferences)

    override fun observePreferences(): Flow<UserPreferences> =
        preferences

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        preferences.update { current ->
            current.copy(themeMode = themeMode)
        }
    }

    override suspend fun setDynamicColorEnabled(enabled: Boolean) {
        preferences.update { current ->
            current.copy(dynamicColorEnabled = enabled)
        }
    }

    override suspend fun setReducedMotionEnabled(enabled: Boolean) {
        preferences.update { current ->
            current.copy(reducedMotionEnabled = enabled)
        }
    }

    override suspend fun setLastSelectedSection(sectionKey: SectionKey?) {
        preferences.update { current ->
            current.copy(lastSelectedSectionKey = sectionKey)
        }
    }

    override suspend fun setHasSeenIntro(seen: Boolean) {
        preferences.update { current ->
            current.copy(hasSeenIntro = seen)
        }
    }
}