package com.pathstoftech.calmexecution.di

import com.pathstoftech.calmexecution.core.data.catalog.CatalogRepository
import com.pathstoftech.calmexecution.core.data.catalog.CatalogRepositoryImpl
import com.pathstoftech.calmexecution.core.data.journey.JourneyRepository
import com.pathstoftech.calmexecution.core.data.journey.JourneyRepositoryImpl
import com.pathstoftech.calmexecution.core.data.preferences.PreferencesRepository
import com.pathstoftech.calmexecution.core.data.preferences.PreferencesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCatalogRepository(
        implementation: CatalogRepositoryImpl
    ): CatalogRepository

    @Binds
    @Singleton
    abstract fun bindJourneyRepository(
        implementation: JourneyRepositoryImpl
    ): JourneyRepository

    @Binds
    @Singleton
    abstract fun bindPreferencesRepository(
        implementation: PreferencesRepositoryImpl
    ): PreferencesRepository
}