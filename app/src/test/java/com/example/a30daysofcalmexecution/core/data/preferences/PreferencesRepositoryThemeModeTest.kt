package com.example.a30daysofcalmexecution.core.data.preferences

import com.example.a30daysofcalmexecution.core.data.preferences.proto.ThemeModeProto
import com.example.a30daysofcalmexecution.core.data.preferences.proto.UserPreferencesProto
import com.example.a30daysofcalmexecution.core.model.SectionKey
import com.example.a30daysofcalmexecution.core.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PreferencesRepositoryThemeModeTest {

    private val mapper = PreferencesMapper()

    @Test
    fun `setThemeMode system sets theme mode to system`() {
        runBlocking {
            val dataSource = FakePreferencesDataSource(
                initialPreferences = UserPreferencesSerializer().defaultValue
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setThemeMode(ThemeMode.SYSTEM)

            val preferences = repository.observePreferences().first()

            assertEquals(ThemeMode.SYSTEM, preferences.themeMode)
        }
    }

    @Test
    fun `setThemeMode light sets theme mode to light`() {
        runBlocking {
            val dataSource = FakePreferencesDataSource(
                initialPreferences = UserPreferencesSerializer().defaultValue
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setThemeMode(ThemeMode.LIGHT)

            val preferences = repository.observePreferences().first()

            assertEquals(ThemeMode.LIGHT, preferences.themeMode)
        }
    }

    @Test
    fun `setThemeMode dark sets theme mode to dark`() {
        runBlocking {
            val dataSource = FakePreferencesDataSource(
                initialPreferences = UserPreferencesSerializer().defaultValue
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setThemeMode(ThemeMode.DARK)

            val preferences = repository.observePreferences().first()

            assertEquals(ThemeMode.DARK, preferences.themeMode)
        }
    }

    @Test
    fun `setThemeMode updates existing theme mode`() {
        runBlocking {
            val initialPreferences = UserPreferencesProto.newBuilder()
                .setThemeMode(ThemeModeProto.THEME_MODE_DARK)
                .build()

            val dataSource = FakePreferencesDataSource(
                initialPreferences = initialPreferences
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setThemeMode(ThemeMode.LIGHT)

            val preferences = repository.observePreferences().first()

            assertEquals(ThemeMode.LIGHT, preferences.themeMode)
        }
    }

    @Test
    fun `setThemeMode preserves dynamic color reduced motion selected section and intro flag`() {
        runBlocking {
            val initialPreferences = UserPreferencesProto.newBuilder()
                .setThemeMode(ThemeModeProto.THEME_MODE_SYSTEM)
                .setDynamicColorEnabled(true)
                .setReducedMotionEnabled(true)
                .setLastSelectedSectionKey("build_focus")
                .setHasSeenIntro(true)
                .build()

            val dataSource = FakePreferencesDataSource(
                initialPreferences = initialPreferences
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setThemeMode(ThemeMode.DARK)

            val preferences = repository.observePreferences().first()

            assertEquals(ThemeMode.DARK, preferences.themeMode)
            assertTrue(preferences.dynamicColorEnabled)
            assertTrue(preferences.reducedMotionEnabled)
            assertEquals(SectionKey.BUILD_FOCUS, preferences.lastSelectedSectionKey)
            assertTrue(preferences.hasSeenIntro)
        }
    }

    @Test
    fun `setThemeMode persists theme mode into preferences data source`() {
        runBlocking {
            val dataSource = FakePreferencesDataSource(
                initialPreferences = UserPreferencesSerializer().defaultValue
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setThemeMode(ThemeMode.DARK)

            val storedProto = dataSource.userPreferences.first()

            assertEquals(ThemeModeProto.THEME_MODE_DARK, storedProto.themeMode)
            assertFalse(storedProto.dynamicColorEnabled)
            assertFalse(storedProto.reducedMotionEnabled)
            assertEquals("", storedProto.lastSelectedSectionKey)
            assertFalse(storedProto.hasSeenIntro)
        }
    }

    private class FakePreferencesDataSource(
        initialPreferences: UserPreferencesProto
    ) : PreferencesDataSource {

        private val state = MutableStateFlow(initialPreferences)

        override val userPreferences: Flow<UserPreferencesProto> =
            state.asStateFlow()

        override suspend fun updateUserPreferences(
            transform: suspend (UserPreferencesProto) -> UserPreferencesProto
        ): UserPreferencesProto {
            val updated = transform(state.value)
            state.value = updated
            return updated
        }
    }
}