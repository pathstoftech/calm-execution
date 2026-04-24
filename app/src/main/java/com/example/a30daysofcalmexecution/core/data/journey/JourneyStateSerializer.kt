package com.example.a30daysofcalmexecution.core.data.journey

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import com.example.a30daysofcalmexecution.core.data.journey.proto.JourneyStateProto

class JourneyStateSerializer @Inject constructor() : Serializer<JourneyStateProto> {
    override val defaultValue: JourneyStateProto =
        JourneyStateProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): JourneyStateProto {
        return try {
            JourneyStateProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException(
                message = "Cannot read journey state proto.",
                cause = exception
            )
        }
    }

    override suspend fun writeTo(t: JourneyStateProto, output: OutputStream) {
        t.writeTo(output)
    }
}