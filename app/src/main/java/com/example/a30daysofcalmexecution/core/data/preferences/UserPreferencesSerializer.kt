package com.example.a30daysofcalmexecution.core.data.preferences

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.a30daysofcalmexecution.core.data.preferences.proto.ThemeModeProto
import com.example.a30daysofcalmexecution.core.data.preferences.proto.UserPreferencesProto
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class UserPreferencesSerializer @Inject constructor() : Serializer<UserPreferencesProto> {

    override val defaultValue: UserPreferencesProto =
        UserPreferencesProto.newBuilder()
            .setThemeMode(ThemeModeProto.THEME_MODE_SYSTEM)
            .setDynamicColorEnabled(false)
            .setReducedMotionEnabled(false)
            .setLastSelectedSectionKey("")
            .setHasSeenIntro(false)
            .build()

    override suspend fun readFrom(input: InputStream): UserPreferencesProto {
        return try {
            UserPreferencesProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException(
                message = "Cannot read user preferences proto.",
                cause = exception
            )
        }
    }

    override suspend fun writeTo(
        t: UserPreferencesProto,
        output: OutputStream
    ) {
        t.writeTo(output)
    }
}