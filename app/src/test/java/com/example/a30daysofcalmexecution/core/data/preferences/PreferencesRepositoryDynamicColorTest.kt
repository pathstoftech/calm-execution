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

class PreferencesRepositoryDynamicColorTest {

    private val mapper = PreferencesMapper()

    @Test
    fun `setDynamicColorEnabled true enables dynamic color`() {
        runBlocking {
            val dataSource = FakePreferencesDataSource(
                initialPreferences = UserPreferencesSerializer().defaultValue
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setDynamicColorEnabled(true)

            val preferences = repository.observePreferences().first()

            assertTrue(preferences.dynamicColorEnabled)
        }
    }

    @Test
    fun `setDynamicColorEnabled false disables dynamic color`() {
        runBlocking {
            val initialPreferences = UserPreferencesProto.newBuilder()
                .setDynamicColorEnabled(true)
                .build()

            val dataSource = FakePreferencesDataSource(
                initialPreferences = initialPreferences
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setDynamicColorEnabled(false)

            val preferences = repository.observePreferences().first()

            assertFalse(preferences.dynamicColorEnabled)
        }
    }

    @Test
    fun `setDynamicColorEnabled updates existing dynamic color value`() {
        runBlocking {
            val initialPreferences = UserPreferencesProto.newBuilder()
                .setDynamicColorEnabled(false)
                .build()

            val dataSource = FakePreferencesDataSource(
                initialPreferences = initialPreferences
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setDynamicColorEnabled(true)

            val preferences = repository.observePreferences().first()

            assertTrue(preferences.dynamicColorEnabled)
        }
    }

    @Test
    fun `setDynamicColorEnabled preserves theme reduced motion selected section and intro flag`() {
        runBlocking {
            val initialPreferences = UserPreferencesProto.newBuilder()
                .setThemeMode(ThemeModeProto.THEME_MODE_DARK)
                .setDynamicColorEnabled(false)
                .setReducedMotionEnabled(true)
                .setLastSelectedSectionKey("protect_boundaries")
                .setHasSeenIntro(true)
                .build()

            val dataSource = FakePreferencesDataSource(
                initialPreferences = initialPreferences
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setDynamicColorEnabled(true)

            val preferences = repository.observePreferences().first()

            assertEquals(ThemeMode.DARK, preferences.themeMode)
            assertTrue(preferences.dynamicColorEnabled)
            assertTrue(preferences.reducedMotionEnabled)
            assertEquals(SectionKey.PROTECT_BOUNDARIES, preferences.lastSelectedSectionKey)
            assertTrue(preferences.hasSeenIntro)
        }
    }

    @Test
    fun `setDynamicColorEnabled persists dynamic color into preferences data source`() {
        runBlocking {
            val dataSource = FakePreferencesDataSource(
                initialPreferences = UserPreferencesSerializer().defaultValue
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setDynamicColorEnabled(true)

            val storedProto = dataSource.userPreferences.first()

            assertTrue(storedProto.dynamicColorEnabled)
            assertEquals(ThemeModeProto.THEME_MODE_SYSTEM, storedProto.themeMode)
            assertFalse(storedProto.reducedMotionEnabled)
            assertEquals("", storedProto.lastSelectedSectionKey)
            assertFalse(storedProto.hasSeenIntro)
        }
    }

    @Test
    fun `setDynamicColorEnabled does not expose proto to repository consumers`() {
        runBlocking {
            val dataSource = FakePreferencesDataSource(
                initialPreferences = UserPreferencesSerializer().defaultValue
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setDynamicColorEnabled(true)

            val preferences = repository.observePreferences().first()

            assertTrue(preferences.dynamicColorEnabled)
            assertEquals(ThemeMode.SYSTEM, preferences.themeMode)
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