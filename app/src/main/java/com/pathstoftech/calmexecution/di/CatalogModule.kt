package com.pathstoftech.calmexecution.di

import com.pathstoftech.calmexecution.core.data.catalog.CatalogDataSource
import com.pathstoftech.calmexecution.core.data.catalog.RawResourceCatalogDataSource
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