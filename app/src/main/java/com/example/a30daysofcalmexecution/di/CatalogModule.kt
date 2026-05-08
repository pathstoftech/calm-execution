package com.example.a30daysofcalmexecution.di

import com.example.a30daysofcalmexecution.core.data.catalog.CatalogDataSource
import com.example.a30daysofcalmexecution.core.data.catalog.RawResourceCatalogDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CatalogModule {

    @Binds
    @Singleton
    abstract fun bindCatalogDataSource(
        implementation: RawResourceCatalogDataSource
    ): CatalogDataSource
}