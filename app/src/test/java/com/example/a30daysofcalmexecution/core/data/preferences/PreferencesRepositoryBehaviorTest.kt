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

class PreferencesRepositoryBehaviorTest {

    private val mapper = PreferencesMapper()

    @Test
    fun `observePreferences exposes default domain preferences from default proto`() {
        runBlocking {
            val repository = createRepository(
                dataSource = FakePreferencesDataSource(
                    initialPreferences = UserPreferencesProto.getDefaultInstance()
                )
            )

            val preferences = repository.observePreferences().first()

            assertEquals(ThemeMode.SYSTEM, preferences.themeMode)
            assertFalse(preferences.dynamicColorEnabled)
            assertFalse(preferences.reducedMotionEnabled)
            assertNull(preferences.lastSelectedSectionKey)
            assertFalse(preferences.hasSeenIntro)
        }
    }

    @Test
    fun `repository supports realistic preferences lifecycle`() {
        runBlocking {
            val dataSource = FakePreferencesDataSource(
                initialPreferences = UserPreferencesProto.getDefaultInstance()
            )
            val repository = createRepository(dataSource)

            repository.setThemeMode(ThemeMode.DARK)
            repository.setDynamicColorEnabled(true)
            repository.setReducedMotionEnabled(true)
            repository.setLastSelectedSection(SectionKey.BUILD_FOCUS)
            repository.setHasSeenIntro(true)

            val preferences = repository.observePreferences().first()

            assertEquals(ThemeMode.DARK, preferences.themeMode)
            assertTrue(preferences.dynamicColorEnabled)
            assertTrue(preferences.reducedMotionEnabled)
            assertEquals(SectionKey.BUILD_FOCUS, preferences.lastSelectedSectionKey)
            assertTrue(preferences.hasSeenIntro)

            val storedProto = dataSource.userPreferences.first()

            assertEquals(ThemeModeProto.THEME_MODE_DARK, storedProto.themeMode)
            assertTrue(storedProto.dynamicColorEnabled)
            assertTrue(storedProto.reducedMotionEnabled)
            assertEquals("build_focus", storedProto.lastSelectedSectionKey)
            assertTrue(storedProto.hasSeenIntro)
        }
    }

    @Test
    fun `mutating one preference preserves other preference values`() {
        runBlocking {
            val initialPreferences = UserPreferencesProto.newBuilder()
                .setThemeMode(ThemeModeProto.THEME_MODE_LIGHT)
                .setDynamicColorEnabled(true)
                .setReducedMotionEnabled(true)
                .setLastSelectedSectionKey("protect_boundaries")
                .setHasSeenIntro(true)
                .build()

            val repository = createRepository(
                dataSource = FakePreferencesDataSource(initialPreferences)
            )

            repository.setThemeMode(ThemeMode.DARK)

            val preferencesAfterThemeChange = repository.observePreferences().first()

            assertEquals(ThemeMode.DARK, preferencesAfterThemeChange.themeMode)
            assertTrue(preferencesAfterThemeChange.dynamicColorEnabled)
            assertTrue(preferencesAfterThemeChange.reducedMotionEnabled)
            assertEquals(
                SectionKey.PROTECT_BOUNDARIES,
                preferencesAfterThemeChange.lastSelectedSectionKey
            )
            assertTrue(preferencesAfterThemeChange.hasSeenIntro)

            repository.setReducedMotionEnabled(false)

            val preferencesAfterMotionChange = repository.observePreferences().first()

            assertEquals(ThemeMode.DARK, preferencesAfterMotionChange.themeMode)
            assertTrue(preferencesAfterMotionChange.dynamicColorEnabled)
            assertFalse(preferencesAfterMotionChange.reducedMotionEnabled)
            assertEquals(
                SectionKey.PROTECT_BOUNDARIES,
                preferencesAfterMotionChange.lastSelectedSectionKey
            )
            assertTrue(preferencesAfterMotionChange.hasSeenIntro)
        }
    }

    @Test
    fun `selected section can be changed and cleared through repository`() {
        runBlocking {
            val dataSource = FakePreferencesDataSource(
                initialPreferences = UserPreferencesProto.getDefaultInstance()
            )
            val repository = createRepository(dataSource)

            repository.setLastSelectedSection(SectionKey.SUSTAIN_ENERGY)

            val selectedPreferences = repository.observePreferences().first()

            assertEquals(SectionKey.SUSTAIN_ENERGY, selectedPreferences.lastSelectedSectionKey)
            assertEquals(
                "sustain_energy",
                dataSource.userPreferences.first().lastSelectedSectionKey
            )

            repository.setLastSelectedSection(null)

            val clearedPreferences = repository.observePreferences().first()
            val storedProto = dataSource.userPreferences.first()

            assertNull(clearedPreferences.lastSelectedSectionKey)
            assertEquals("", storedProto.lastSelectedSectionKey)
        }
    }

    @Test
    fun `repository safely exposes null selected section for unknown stored section key`() {
        runBlocking {
            val initialPreferences = UserPreferencesProto.newBuilder()
                .setThemeMode(ThemeModeProto.THEME_MODE_SYSTEM)
                .setLastSelectedSectionKey("not_a_real_section")
                .build()

            val repository = createRepository(
                dataSource = FakePreferencesDataSource(initialPreferences)
            )

            val preferences = repository.observePreferences().first()

            assertEquals(ThemeMode.SYSTEM, preferences.themeMode)
            assertNull(preferences.lastSelectedSectionKey)
        }
    }

    @Test
    fun `repository maps unspecified stored theme to system theme`() {
        runBlocking {
            val initialPreferences = UserPreferencesProto.newBuilder()
                .setThemeMode(ThemeModeProto.THEME_MODE_UNSPECIFIED)
                .setDynamicColorEnabled(true)
                .build()

            val repository = createRepository(
                dataSource = FakePreferencesDataSource(initialPreferences)
            )

            val preferences = repository.observePreferences().first()

            assertEquals(ThemeMode.SYSTEM, preferences.themeMode)
            assertTrue(preferences.dynamicColorEnabled)
        }
    }

    @Test
    fun `repository can disable all optional preferences after they were enabled`() {
        runBlocking {
            val initialPreferences = UserPreferencesProto.newBuilder()
                .setThemeMode(ThemeModeProto.THEME_MODE_DARK)
                .setDynamicColorEnabled(true)
                .setReducedMotionEnabled(true)
                .setLastSelectedSectionKey("finish_and_improve")
                .setHasSeenIntro(true)
                .build()

            val dataSource = FakePreferencesDataSource(initialPreferences)
            val repository = createRepository(dataSource)

            repository.setThemeMode(ThemeMode.SYSTEM)
            repository.setDynamicColorEnabled(false)
            repository.setReducedMotionEnabled(false)
            repository.setLastSelectedSection(null)
            repository.setHasSeenIntro(false)

            val preferences = repository.observePreferences().first()
            val storedProto = dataSource.userPreferences.first()

            assertEquals(ThemeMode.SYSTEM, preferences.themeMode)
            assertFalse(preferences.dynamicColorEnabled)
            assertFalse(preferences.reducedMotionEnabled)
            assertNull(preferences.lastSelectedSectionKey)
            assertFalse(preferences.hasSeenIntro)

            assertEquals(ThemeModeProto.THEME_MODE_SYSTEM, storedProto.themeMode)
            assertFalse(storedProto.dynamicColorEnabled)
            assertFalse(storedProto.reducedMotionEnabled)
            assertEquals("", storedProto.lastSelectedSectionKey)
            assertFalse(storedProto.hasSeenIntro)
        }
    }

    private fun createRepository(
        dataSource: PreferencesDataSource
    ): PreferencesRepository {
        return PreferencesRepositoryImpl(
            dataSource = dataSource,
            mapper = mapper
        )
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