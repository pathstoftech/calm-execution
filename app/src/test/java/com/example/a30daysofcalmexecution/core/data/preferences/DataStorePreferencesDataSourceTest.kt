package com.example.a30daysofcalmexecution.core.data.preferences

import androidx.datastore.core.DataStoreFactory
import com.example.a30daysofcalmexecution.core.data.preferences.proto.ThemeModeProto
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class DataStorePreferencesDataSourceTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private val dataStoreJobs = mutableListOf<Job>()

    @After
    fun tearDown() {
        dataStoreJobs.forEach { job ->
            job.cancel()
        }
        dataStoreJobs.clear()
    }

    @Test
    fun `userPreferences emits default value before updates`() {
        runBlocking {
            val dataSource = createDataSource(fileName = "default_user_preferences.pb")

            val preferences = dataSource.userPreferences.first()

            assertEquals(ThemeModeProto.THEME_MODE_SYSTEM, preferences.themeMode)
            assertFalse(preferences.dynamicColorEnabled)
            assertFalse(preferences.reducedMotionEnabled)
            assertEquals("", preferences.lastSelectedSectionKey)
            assertFalse(preferences.hasSeenIntro)
        }
    }

    @Test
    fun `updateUserPreferences persists theme mode`() {
        runBlocking {
            val dataSource = createDataSource(fileName = "theme_mode_user_preferences.pb")

            val updated = dataSource.updateUserPreferences { current ->
                current.toBuilder()
                    .setThemeMode(ThemeModeProto.THEME_MODE_DARK)
                    .build()
            }

            val restored = dataSource.userPreferences.first()

            assertEquals(ThemeModeProto.THEME_MODE_DARK, updated.themeMode)
            assertEquals(updated, restored)
        }
    }

    @Test
    fun `updateUserPreferences persists boolean preferences`() {
        runBlocking {
            val dataSource = createDataSource(fileName = "boolean_user_preferences.pb")

            val updated = dataSource.updateUserPreferences { current ->
                current.toBuilder()
                    .setDynamicColorEnabled(true)
                    .setReducedMotionEnabled(true)
                    .setHasSeenIntro(true)
                    .build()
            }

            val restored = dataSource.userPreferences.first()

            assertTrue(updated.dynamicColorEnabled)
            assertTrue(updated.reducedMotionEnabled)
            assertTrue(updated.hasSeenIntro)

            assertEquals(updated, restored)
            assertTrue(restored.dynamicColorEnabled)
            assertTrue(restored.reducedMotionEnabled)
            assertTrue(restored.hasSeenIntro)
        }
    }

    @Test
    fun `updateUserPreferences persists selected section key`() {
        runBlocking {
            val dataSource = createDataSource(fileName = "selected_section_user_preferences.pb")

            val updated = dataSource.updateUserPreferences { current ->
                current.toBuilder()
                    .setLastSelectedSectionKey("build_focus")
                    .build()
            }

            val restored = dataSource.userPreferences.first()

            assertEquals("build_focus", updated.lastSelectedSectionKey)
            assertEquals(updated, restored)
            assertEquals("build_focus", restored.lastSelectedSectionKey)
        }
    }

    @Test
    fun `updateUserPreferences can persist blank selected section key`() {
        runBlocking {
            val dataSource = createDataSource(fileName = "blank_selected_section_user_preferences.pb")

            val updated = dataSource.updateUserPreferences { current ->
                current.toBuilder()
                    .clearLastSelectedSectionKey()
                    .build()
            }

            val restored = dataSource.userPreferences.first()

            assertEquals("", updated.lastSelectedSectionKey)
            assertEquals(updated, restored)
            assertEquals("", restored.lastSelectedSectionKey)
        }
    }

    private fun createDataSource(fileName: String): PreferencesDataSource {
        val file = File(temporaryFolder.root, fileName)
        val job = SupervisorJob()
        dataStoreJobs += job

        val dataStore = DataStoreFactory.create(
            serializer = UserPreferencesSerializer(),
            scope = CoroutineScope(Dispatchers.IO + job),
            produceFile = {
                file
            }
        )

        return DataStorePreferencesDataSource(
            dataStore = dataStore
        )
    }
}