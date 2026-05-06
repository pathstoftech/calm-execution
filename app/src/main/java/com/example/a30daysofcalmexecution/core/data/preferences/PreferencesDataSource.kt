package com.example.a30daysofcalmexecution.core.data.preferences

import com.example.a30daysofcalmexecution.core.data.preferences.proto.UserPreferencesProto
import kotlinx.coroutines.flow.Flow

interface PreferencesDataSource {
    val userPreferences: Flow<UserPreferencesProto>

    suspend fun updateUserPreferences(
        transform: suspend (UserPreferencesProto) -> UserPreferencesProto
    ): UserPreferencesProto
}