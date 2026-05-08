package com.example.a30daysofcalmexecution.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.example.a30daysofcalmexecution.core.data.journey.JourneyStateSerializer
import com.example.a30daysofcalmexecution.core.data.journey.proto.JourneyStateProto
import com.example.a30daysofcalmexecution.core.data.preferences.UserPreferencesSerializer
import com.example.a30daysofcalmexecution.core.data.preferences.proto.UserPreferencesProto
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    private const val JOURNEY_STATE_FILE_NAME = "journey_state.pb"
    private const val USER_PREFERENCES_FILE_NAME = "user_preferences.pb"

    @Provides
    @Singleton
    fun provideJourneyStateDataStore(
        @ApplicationContext context: Context,
        serializer: JourneyStateSerializer
    ): DataStore<JourneyStateProto> {
        return DataStoreFactory.create(
            serializer = serializer,
            produceFile = {
                context.dataStoreFile(JOURNEY_STATE_FILE_NAME)
            }
        )
    }

    @Provides
    @Singleton
    fun provideUserPreferencesDataStore(
        @ApplicationContext context: Context,
        serializer: UserPreferencesSerializer
    ): DataStore<UserPreferencesProto> {
        return DataStoreFactory.create(
            serializer = serializer,
            produceFile = {
                context.dataStoreFile(USER_PREFERENCES_FILE_NAME)
            }
        )
    }
}