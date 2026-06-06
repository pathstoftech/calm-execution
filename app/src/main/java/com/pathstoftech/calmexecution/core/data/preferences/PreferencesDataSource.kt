package com.pathstoftech.calmexecution.core.data.preferences

import com.pathstoftech.calmexecution.core.data.preferences.proto.UserPreferencesProto
import kotlinx.coroutines.flow.Flow

interface PreferencesDataSource {
    val userPreferences: Flow<UserPreferencesProto>

    suspend fun updateUserPreferences(
        transform: suspend (UserPreferencesProto) -> UserPreferencesProto
    ): UserPreferencesProto
}