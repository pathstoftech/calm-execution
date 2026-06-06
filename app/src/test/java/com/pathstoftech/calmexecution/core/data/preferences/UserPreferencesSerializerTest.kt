package com.pathstoftech.calmexecution.core.data.preferences

import androidx.datastore.core.CorruptionException
import com.pathstoftech.calmexecution.core.data.preferences.proto.ThemeModeProto
import com.pathstoftech.calmexecution.core.data.preferences.proto.UserPreferencesProto
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class UserPreferencesSerializerTest {

    private val serializer = UserPreferencesSerializer()

    @Test
    fun `defaultValue returns app preference defaults`() {
        val defaultValue = serializer.defaultValue

        assertEquals(ThemeModeProto.THEME_MODE_SYSTEM, defaultValue.themeMode)
        assertFalse(defaultValue.dynamicColorEnabled)
        assertFalse(defaultValue.reducedMotionEnabled)
        assertEquals("", defaultValue.lastSelectedSectionKey)
        assertFalse(defaultValue.hasSeenIntro)
    }

    @Test
    fun `writeTo and readFrom round trip user preferences proto`() {
        runBlocking {
            val original = UserPreferencesProto.newBuilder()
                .setThemeMode(ThemeModeProto.THEME_MODE_DARK)
                .setDynamicColorEnabled(true)
                .setReducedMotionEnabled(true)
                .setLastSelectedSectionKey("build_focus")
                .setHasSeenIntro(true)
                .build()

            val output = ByteArrayOutputStream()

            serializer.writeTo(
                t = original,
                output = output
            )

            val restored = serializer.readFrom(
                input = ByteArrayInputStream(output.toByteArray())
            )

            assertEquals(original, restored)
        }
    }

    @Test(expected = CorruptionException::class)
    fun `readFrom throws CorruptionException for invalid proto bytes`() {
        runBlocking {
            serializer.readFrom(
                input = ByteArrayInputStream(byteArrayOf(0xFF.toByte()))
            )
        }
    }
}