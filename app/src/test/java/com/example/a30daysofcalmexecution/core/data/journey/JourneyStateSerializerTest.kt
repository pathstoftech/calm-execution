package com.example.a30daysofcalmexecution.core.data.journey

import androidx.datastore.core.CorruptionException
import com.example.a30daysofcalmexecution.core.data.journey.proto.JourneyStateProto
import com.example.a30daysofcalmexecution.core.data.journey.proto.TipCompletionStatusProto
import com.example.a30daysofcalmexecution.core.data.journey.proto.TipUserStateProto
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class JourneyStateSerializerTest {

    private val serializer = JourneyStateSerializer()

    @Test
    fun `defaultValue returns empty journey state proto`() {
        assertEquals(
            JourneyStateProto.getDefaultInstance(),
            serializer.defaultValue
        )
    }

    @Test
    fun `writeTo and readFrom round trip journey state proto`() = runBlocking {
        val original = JourneyStateProto.newBuilder()
            .setActiveTipId("day_01_define_real_priority")
            .addTipStates(
                TipUserStateProto.newBuilder()
                    .setTipId("day_01_define_real_priority")
                    .setCompletionStatus(TipCompletionStatusProto.IN_PROGRESS)
                    .setIsBookmarked(true)
                    .setLastViewedAtEpochMillis(123L)
                    .build()
            )
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

    @Test(expected = CorruptionException::class)
    fun `readFrom throws CorruptionException for invalid proto bytes`() {
        runBlocking {
            serializer.readFrom(
                input = ByteArrayInputStream(byteArrayOf(0xFF.toByte()))
            )
        }
    }
}