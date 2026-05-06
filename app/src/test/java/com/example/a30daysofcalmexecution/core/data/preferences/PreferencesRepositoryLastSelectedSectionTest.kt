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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PreferencesRepositorySelectedSectionTest {

    private val mapper = PreferencesMapper()

    @Test
    fun `setLastSelectedSection stores selected section`() {
        runBlocking {
            val dataSource = FakePreferencesDataSource(
                initialPreferences = UserPreferencesSerializer().defaultValue
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setLastSelectedSection(SectionKey.BUILD_FOCUS)

            val preferences = repository.observePreferences().first()

            assertEquals(SectionKey.BUILD_FOCUS, preferences.lastSelectedSectionKey)
        }
    }

    @Test
    fun `setLastSelectedSection updates existing selected section`() {
        runBlocking {
            val initialPreferences = UserPreferencesProto.newBuilder()
                .setLastSelectedSectionKey("start_with_clarity")
                .build()

            val dataSource = FakePreferencesDataSource(
                initialPreferences = initialPreferences
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setLastSelectedSection(SectionKey.PROTECT_BOUNDARIES)

            val preferences = repository.observePreferences().first()

            assertEquals(SectionKey.PROTECT_BOUNDARIES, preferences.lastSelectedSectionKey)
        }
    }

    @Test
    fun `setLastSelectedSection null clears selected section`() {
        runBlocking {
            val initialPreferences = UserPreferencesProto.newBuilder()
                .setLastSelectedSectionKey("build_focus")
                .build()

            val dataSource = FakePreferencesDataSource(
                initialPreferences = initialPreferences
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setLastSelectedSection(null)

            val preferences = repository.observePreferences().first()

            assertNull(preferences.lastSelectedSectionKey)
        }
    }

    @Test
    fun `setLastSelectedSection can store every section key`() {
        runBlocking {
            val dataSource = FakePreferencesDataSource(
                initialPreferences = UserPreferencesSerializer().defaultValue
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            SectionKey.entries.forEach { sectionKey ->
                repository.setLastSelectedSection(sectionKey)

                val preferences = repository.observePreferences().first()

                assertEquals(sectionKey, preferences.lastSelectedSectionKey)
            }
        }
    }

    @Test
    fun `setLastSelectedSection preserves theme dynamic color reduced motion and intro flag`() {
        runBlocking {
            val initialPreferences = UserPreferencesProto.newBuilder()
                .setThemeMode(ThemeModeProto.THEME_MODE_DARK)
                .setDynamicColorEnabled(true)
                .setReducedMotionEnabled(true)
                .setLastSelectedSectionKey("start_with_clarity")
                .setHasSeenIntro(true)
                .build()

            val dataSource = FakePreferencesDataSource(
                initialPreferences = initialPreferences
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setLastSelectedSection(SectionKey.FINISH_AND_IMPROVE)

            val preferences = repository.observePreferences().first()

            assertEquals(ThemeMode.DARK, preferences.themeMode)
            assertTrue(preferences.dynamicColorEnabled)
            assertTrue(preferences.reducedMotionEnabled)
            assertEquals(SectionKey.FINISH_AND_IMPROVE, preferences.lastSelectedSectionKey)
            assertTrue(preferences.hasSeenIntro)
        }
    }

    @Test
    fun `setLastSelectedSection persists selected section into preferences data source`() {
        runBlocking {
            val dataSource = FakePreferencesDataSource(
                initialPreferences = UserPreferencesSerializer().defaultValue
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setLastSelectedSection(SectionKey.SUSTAIN_ENERGY)

            val storedProto = dataSource.userPreferences.first()

            assertEquals("sustain_energy", storedProto.lastSelectedSectionKey)
            assertEquals(ThemeModeProto.THEME_MODE_SYSTEM, storedProto.themeMode)
            assertFalse(storedProto.dynamicColorEnabled)
            assertFalse(storedProto.reducedMotionEnabled)
            assertFalse(storedProto.hasSeenIntro)
        }
    }

    @Test
    fun `setLastSelectedSection null persists blank selected section into preferences data source`() {
        runBlocking {
            val initialPreferences = UserPreferencesProto.newBuilder()
                .setLastSelectedSectionKey("build_focus")
                .build()

            val dataSource = FakePreferencesDataSource(
                initialPreferences = initialPreferences
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setLastSelectedSection(null)

            val storedProto = dataSource.userPreferences.first()

            assertEquals("", storedProto.lastSelectedSectionKey)
        }
    }

    @Test
    fun `setLastSelectedSection does not expose proto to repository consumers`() {
        runBlocking {
            val dataSource = FakePreferencesDataSource(
                initialPreferences = UserPreferencesSerializer().defaultValue
            )
            val repository = PreferencesRepositoryImpl(
                dataSource = dataSource,
                mapper = mapper
            )

            repository.setLastSelectedSection(SectionKey.BUILD_FOCUS)

            val preferences = repository.observePreferences().first()

            assertEquals(SectionKey.BUILD_FOCUS, preferences.lastSelectedSectionKey)
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