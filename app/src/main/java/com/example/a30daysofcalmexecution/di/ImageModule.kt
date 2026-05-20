package com.example.a30daysofcalmexecution.di

import com.example.a30daysofcalmexecution.core.data.images.DrawableTipImageResolver
import com.example.a30daysofcalmexecution.core.data.images.TipImageResolver
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