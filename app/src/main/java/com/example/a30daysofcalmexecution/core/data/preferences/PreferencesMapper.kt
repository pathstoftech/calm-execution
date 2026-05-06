package com.example.a30daysofcalmexecution.core.data.preferences

import com.example.a30daysofcalmexecution.core.data.preferences.proto.ThemeModeProto
import com.example.a30daysofcalmexecution.core.data.preferences.proto.UserPreferencesProto
import com.example.a30daysofcalmexecution.core.model.SectionKey
import com.example.a30daysofcalmexecution.core.model.ThemeMode
import com.example.a30daysofcalmexecution.core.model.UserPreferences
import javax.inject.Inject

class PreferencesMapper @Inject constructor() {

    fun toDomain(proto: UserPreferencesProto): UserPreferences {
        return UserPreferences(
            themeMode = proto.themeMode.toDomain(),
            dynamicColorEnabled = proto.dynamicColorEnabled,
            reducedMotionEnabled = proto.reducedMotionEnabled,
            lastSelectedSectionKey = proto.lastSelectedSectionKey.toSectionKeyOrNull(),
            hasSeenIntro = proto.hasSeenIntro
        )
    }

    fun toProto(domain: UserPreferences): UserPreferencesProto {
        return UserPreferencesProto.newBuilder()
            .setThemeMode(domain.themeMode.toProto())
            .setDynamicColorEnabled(domain.dynamicColorEnabled)
            .setReducedMotionEnabled(domain.reducedMotionEnabled)
            .setLastSelectedSectionKey(domain.lastSelectedSectionKey?.wireValue.orEmpty())
            .setHasSeenIntro(domain.hasSeenIntro)
            .build()
    }

    private fun ThemeModeProto.toDomain(): ThemeMode {
        return when (this) {
            ThemeModeProto.THEME_MODE_LIGHT -> ThemeMode.LIGHT
            ThemeModeProto.THEME_MODE_DARK -> ThemeMode.DARK
            ThemeModeProto.THEME_MODE_SYSTEM,
            ThemeModeProto.THEME_MODE_UNSPECIFIED,
            ThemeModeProto.UNRECOGNIZED -> ThemeMode.SYSTEM
        }
    }
    private fun ThemeMode.toProto(): ThemeModeProto {
        return when (this) {
            ThemeMode.SYSTEM -> ThemeModeProto.THEME_MODE_SYSTEM
            ThemeMode.LIGHT -> ThemeModeProto.THEME_MODE_LIGHT
            ThemeMode.DARK -> ThemeModeProto.THEME_MODE_DARK
        }
    }

    private fun String.toSectionKeyOrNull(): SectionKey? {
        return takeIf { it.isNotBlank() }
            ?.let(SectionKey::fromWireValue)
    }
}