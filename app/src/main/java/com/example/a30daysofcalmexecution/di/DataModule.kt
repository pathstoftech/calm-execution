package com.example.a30daysofcalmexecution.di

import com.example.a30daysofcalmexecution.core.data.journey.DataStoreJourneyDataSource
import com.example.a30daysofcalmexecution.core.data.journey.JourneyDataSource
import com.example.a30daysofcalmexecution.core.data.preferences.DataStorePreferencesDataSource
import com.example.a30daysofcalmexecution.core.data.preferences.PreferencesDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindJourneyDataSource(
        implementation: DataStoreJourneyDataSource
    ): JourneyDataSource

    @Binds
    @Singleton
    abstract fun bindPreferencesDataSource(
        implementation: DataStorePreferencesDataSource
    ): PreferencesDataSource
}