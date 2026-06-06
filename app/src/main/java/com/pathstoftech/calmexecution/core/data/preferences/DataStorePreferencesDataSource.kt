package com.pathstoftech.calmexecution.core.data.preferences

import androidx.datastore.core.DataStore
import com.pathstoftech.calmexecution.core.data.preferences.proto.UserPreferencesProto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataStorePreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<UserPreferencesProto>
) : PreferencesDataSource {

    override val userPreferences: Flow<UserPreferencesProto> = dataStore.data

    override suspend fun updateUserPreferences(
        transform: suspend (UserPreferencesProto) -> UserPreferencesProto
    ): UserPreferencesProto {
        return dataStore.updateData(transform)
    }
}