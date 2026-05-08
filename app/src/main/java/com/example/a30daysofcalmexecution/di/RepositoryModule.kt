package com.example.a30daysofcalmexecution.di

import com.example.a30daysofcalmexecution.core.data.catalog.CatalogRepository
import com.example.a30daysofcalmexecution.core.data.catalog.CatalogRepositoryImpl
import com.example.a30daysofcalmexecution.core.data.journey.JourneyRepository
import com.example.a30daysofcalmexecution.core.data.journey.JourneyRepositoryImpl
import com.example.a30daysofcalmexecution.core.data.preferences.PreferencesRepository
import com.example.a30daysofcalmexecution.core.data.preferences.PreferencesRepositoryImpl
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