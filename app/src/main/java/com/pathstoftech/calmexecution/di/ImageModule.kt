package com.pathstoftech.calmexecution.di

import com.pathstoftech.calmexecution.core.data.images.DrawableTipImageResolver
import com.pathstoftech.calmexecution.core.data.images.TipImageResolver
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ImageModule {

    @Binds
    @Singleton
    abstract fun bindTipImageResolver(
        implementation: DrawableTipImageResolver,
    ): TipImageResolver
}