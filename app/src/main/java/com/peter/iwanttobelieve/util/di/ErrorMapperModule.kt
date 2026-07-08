package com.peter.iwanttobelieve.util.di

import com.peter.iwanttobelieve.util.ErrorMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ErrorMapperModule {

    @Provides
    fun provideErrorMapper(): ErrorMapper = ErrorMapper()
}