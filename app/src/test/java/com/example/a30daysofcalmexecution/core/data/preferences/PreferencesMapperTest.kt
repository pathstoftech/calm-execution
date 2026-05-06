package com.example.a30daysofcalmexecution.core.data.preferences

import com.example.a30daysofcalmexecution.core.data.preferences.proto.ThemeModeProto
import com.example.a30daysofcalmexecution.core.data.preferences.proto.UserPreferencesProto
import com.example.a30daysofcalmexecution.core.model.SectionKey
import com.example.a30daysofcalmexecution.core.model.ThemeMode
import com.example.a30daysofcalmexecution.core.model.UserPreferences
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PreferencesMapperTest {

    private val mapper = PreferencesMapper()

    @Test
    fun `toDomain maps default proto to default domain preferences`() {
        val domain = mapper.toDomain(UserPreferencesProto.getDefaultInstance())

        assertEquals(ThemeMode.SYSTEM, domain.themeMode)
        assertFalse(domain.dynamicColorEnabled)
        assertFalse(domain.reducedMotionEnabled)
        assertNull(domain.lastSelectedSectionKey)
        assertFalse(domain.hasSeenIntro)
    }

    @Test
    fun `toDomain maps light theme`() {
        val proto = UserPreferencesProto.newBuilder()
            .setThemeMode(ThemeModeProto.THEME_MODE_LIGHT)
            .build()

        val domain = mapper.toDomain(proto)

        assertEquals(ThemeMode.LIGHT, domain.themeMode)
    }

    @Test
    fun `toDomain maps dark theme`() {
        val proto = UserPreferencesProto.newBuilder()
            .setThemeMode(ThemeModeProto.THEME_MODE_DARK)
            .build()

        val domain = mapper.toDomain(proto)

        assertEquals(ThemeMode.DARK, domain.themeMode)
    }

    @Test
    fun `toDomain maps unspecified theme to system`() {
        val proto = UserPreferencesProto.newBuilder()
            .setThemeMode(ThemeModeProto.THEME_MODE_UNSPECIFIED)
            .build()

        val domain = mapper.toDomain(proto)

        assertEquals(ThemeMode.SYSTEM, domain.themeMode)
    }

    @Test
    fun `toDomain maps boolean preferences`() {
        val proto = UserPreferencesProto.newBuilder()
            .setDynamicColorEnabled(true)
            .setReducedMotionEnabled(true)
            .setHasSeenIntro(true)
            .build()

        val domain = mapper.toDomain(proto)

        assertTrue(domain.dynamicColorEnabled)
        assertTrue(domain.reducedMotionEnabled)
        assertTrue(domain.hasSeenIntro)
    }

    @Test
    fun `toDomain maps selected section key`() {
        val proto = UserPreferencesProto.newBuilder()
            .setLastSelectedSectionKey("build_focus")
            .build()

        val domain = mapper.toDomain(proto)

        assertEquals(SectionKey.BUILD_FOCUS, domain.lastSelectedSectionKey)
    }

    @Test
    fun `toDomain maps blank selected section key to null`() {
        val proto = UserPreferencesProto.newBuilder()
            .setLastSelectedSectionKey("")
            .build()

        val domain = mapper.toDomain(proto)

        assertNull(domain.lastSelectedSectionKey)
    }

    @Test
    fun `toDomain maps unknown selected section key to null`() {
        val proto = UserPreferencesProto.newBuilder()
            .setLastSelectedSectionKey("not_a_real_section")
            .build()

        val domain = mapper.toDomain(proto)

        assertNull(domain.lastSelectedSectionKey)
    }

    @Test
    fun `toProto maps domain preferences to proto`() {
        val domain = UserPreferences(
            themeMode = ThemeMode.DARK,
            dynamicColorEnabled = true,
            reducedMotionEnabled = true,
            lastSelectedSectionKey = SectionKey.BUILD_FOCUS,
            hasSeenIntro = true
        )

        val proto = mapper.toProto(domain)

        assertEquals(ThemeModeProto.THEME_MODE_DARK, proto.themeMode)
        assertTrue(proto.dynamicColorEnabled)
        assertTrue(proto.reducedMotionEnabled)
        assertEquals("build_focus", proto.lastSelectedSectionKey)
        assertTrue(proto.hasSeenIntro)
    }

    @Test
    fun `toProto maps null selected section key to blank string`() {
        val domain = UserPreferences(
            lastSelectedSectionKey = null
        )

        val proto = mapper.toProto(domain)

        assertEquals("", proto.lastSelectedSectionKey)
    }

    @Test
    fun `round trip preserves valid user preferences`() {
        val original = UserPreferences(
            themeMode = ThemeMode.LIGHT,
            dynamicColorEnabled = true,
            reducedMotionEnabled = true,
            lastSelectedSectionKey = SectionKey.PROTECT_BOUNDARIES,
            hasSeenIntro = true
        )

        val restored = mapper.toDomain(mapper.toProto(original))

        assertEquals(original, restored)
    }
}