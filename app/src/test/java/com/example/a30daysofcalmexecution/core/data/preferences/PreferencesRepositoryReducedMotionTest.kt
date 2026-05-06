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

class PreferencesRepositoryReducedMotionTest {

    private val mapper = PreferencesMapper()

    @Test
    fun `setReducedMotionEnabled true enables reduced motion`() {
        runBlocking {
            val dataSource = FakePreferencesDataSource(
                initialPreferences = UserPreferencesSerializer().defaultValue
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setReducedMotionEnabled(true)

            val preferences = repository.observePreferences().first()

            assertTrue(preferences.reducedMotionEnabled)
        }
    }

    @Test
    fun `setReducedMotionEnabled false disables reduced motion`() {
        runBlocking {
            val initialPreferences = UserPreferencesProto.newBuilder()
                .setReducedMotionEnabled(true)
                .build()

            val dataSource = FakePreferencesDataSource(
                initialPreferences = initialPreferences
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setReducedMotionEnabled(false)

            val preferences = repository.observePreferences().first()

            assertFalse(preferences.reducedMotionEnabled)
        }
    }

    @Test
    fun `setReducedMotionEnabled updates existing reduced motion value`() {
        runBlocking {
            val initialPreferences = UserPreferencesProto.newBuilder()
                .setReducedMotionEnabled(false)
                .build()

            val dataSource = FakePreferencesDataSource(
                initialPreferences = initialPreferences
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setReducedMotionEnabled(true)

            val preferences = repository.observePreferences().first()

            assertTrue(preferences.reducedMotionEnabled)
        }
    }

    @Test
    fun `setReducedMotionEnabled preserves theme dynamic color selected section and intro flag`() {
        runBlocking {
            val initialPreferences = UserPreferencesProto.newBuilder()
                .setThemeMode(ThemeModeProto.THEME_MODE_DARK)
                .setDynamicColorEnabled(true)
                .setReducedMotionEnabled(false)
                .setLastSelectedSectionKey("sustain_energy")
                .setHasSeenIntro(true)
                .build()

            val dataSource = FakePreferencesDataSource(
                initialPreferences = initialPreferences
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setReducedMotionEnabled(true)

            val preferences = repository.observePreferences().first()

            assertEquals(ThemeMode.DARK, preferences.themeMode)
            assertTrue(preferences.dynamicColorEnabled)
            assertTrue(preferences.reducedMotionEnabled)
            assertEquals(SectionKey.SUSTAIN_ENERGY, preferences.lastSelectedSectionKey)
            assertTrue(preferences.hasSeenIntro)
        }
    }

    @Test
    fun `setReducedMotionEnabled persists reduced motion into preferences data source`() {
        runBlocking {
            val dataSource = FakePreferencesDataSource(
                initialPreferences = UserPreferencesSerializer().defaultValue
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setReducedMotionEnabled(true)

            val storedProto = dataSource.userPreferences.first()

            assertTrue(storedProto.reducedMotionEnabled)
            assertEquals(ThemeModeProto.THEME_MODE_SYSTEM, storedProto.themeMode)
            assertFalse(storedProto.dynamicColorEnabled)
            assertEquals("", storedProto.lastSelectedSectionKey)
            assertFalse(storedProto.hasSeenIntro)
        }
    }

    @Test
    fun `setReducedMotionEnabled does not expose proto to repository consumers`() {
        runBlocking {
            val dataSource = FakePreferencesDataSource(
                initialPreferences = UserPreferencesSerializer().defaultValue
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setReducedMotionEnabled(true)

            val preferences = repository.observePreferences().first()

            assertTrue(preferences.reducedMotionEnabled)
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