package com.pathstoftech.calmexecution.core.data.preferences

import com.pathstoftech.calmexecution.core.data.preferences.proto.ThemeModeProto
import com.pathstoftech.calmexecution.core.data.preferences.proto.UserPreferencesProto
import com.pathstoftech.calmexecution.core.model.SectionKey
import com.pathstoftech.calmexecution.core.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PreferencesRepositoryIntroSeenTest {

    private val mapper = PreferencesMapper()

    @Test
    fun `setHasSeenIntro true marks intro as seen`() {
        runBlocking {
            val dataSource = FakePreferencesDataSource(
                initialPreferences = UserPreferencesSerializer().defaultValue
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setHasSeenIntro(true)

            val preferences = repository.observePreferences().first()

            assertTrue(preferences.hasSeenIntro)
        }
    }

    @Test
    fun `setHasSeenIntro false marks intro as not seen`() {
        runBlocking {
            val initialPreferences = UserPreferencesProto.newBuilder()
                .setHasSeenIntro(true)
                .build()

            val dataSource = FakePreferencesDataSource(
                initialPreferences = initialPreferences
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setHasSeenIntro(false)

            val preferences = repository.observePreferences().first()

            assertFalse(preferences.hasSeenIntro)
        }
    }

    @Test
    fun `setHasSeenIntro updates existing intro seen value`() {
        runBlocking {
            val initialPreferences = UserPreferencesProto.newBuilder()
                .setHasSeenIntro(false)
                .build()

            val dataSource = FakePreferencesDataSource(
                initialPreferences = initialPreferences
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setHasSeenIntro(true)

            val preferences = repository.observePreferences().first()

            assertTrue(preferences.hasSeenIntro)
        }
    }

    @Test
    fun `setHasSeenIntro preserves theme dynamic color reduced motion and selected section`() {
        runBlocking {
            val initialPreferences = UserPreferencesProto.newBuilder()
                .setThemeMode(ThemeModeProto.THEME_MODE_DARK)
                .setDynamicColorEnabled(true)
                .setReducedMotionEnabled(true)
                .setLastSelectedSectionKey("finish_and_improve")
                .setHasSeenIntro(false)
                .build()

            val dataSource = FakePreferencesDataSource(
                initialPreferences = initialPreferences
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setHasSeenIntro(true)

            val preferences = repository.observePreferences().first()

            assertEquals(ThemeMode.DARK, preferences.themeMode)
            assertTrue(preferences.dynamicColorEnabled)
            assertTrue(preferences.reducedMotionEnabled)
            assertEquals(SectionKey.FINISH_AND_IMPROVE, preferences.lastSelectedSectionKey)
            assertTrue(preferences.hasSeenIntro)
        }
    }

    @Test
    fun `setHasSeenIntro persists intro seen into preferences data source`() {
        runBlocking {
            val dataSource = FakePreferencesDataSource(
                initialPreferences = UserPreferencesSerializer().defaultValue
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setHasSeenIntro(true)

            val storedProto = dataSource.userPreferences.first()

            assertTrue(storedProto.hasSeenIntro)
            assertEquals(ThemeModeProto.THEME_MODE_SYSTEM, storedProto.themeMode)
            assertFalse(storedProto.dynamicColorEnabled)
            assertFalse(storedProto.reducedMotionEnabled)
            assertEquals("", storedProto.lastSelectedSectionKey)
        }
    }

    @Test
    fun `setHasSeenIntro false persists false into preferences data source`() {
        runBlocking {
            val initialPreferences = UserPreferencesProto.newBuilder()
                .setHasSeenIntro(true)
                .build()

            val dataSource = FakePreferencesDataSource(
                initialPreferences = initialPreferences
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setHasSeenIntro(false)

            val storedProto = dataSource.userPreferences.first()

            assertFalse(storedProto.hasSeenIntro)
        }
    }

    @Test
    fun `setHasSeenIntro does not expose proto to repository consumers`() {
        runBlocking {
            val dataSource = FakePreferencesDataSource(
                initialPreferences = UserPreferencesSerializer().defaultValue
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setHasSeenIntro(true)

            val preferences = repository.observePreferences().first()

            assertTrue(preferences.hasSeenIntro)
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