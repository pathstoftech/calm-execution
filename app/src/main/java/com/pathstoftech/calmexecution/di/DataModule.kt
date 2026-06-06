package com.pathstoftech.calmexecution.di

import com.pathstoftech.calmexecution.core.data.journey.DataStoreJourneyDataSource
import com.pathstoftech.calmexecution.core.data.journey.JourneyDataSource
import com.pathstoftech.calmexecution.core.data.preferences.DataStorePreferencesDataSource
import com.pathstoftech.calmexecution.core.data.preferences.PreferencesDataSource
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