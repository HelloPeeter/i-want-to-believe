package com.peter.iwanttobelieve.data.di

import com.peter.iwanttobelieve.data.datasource.PostRemoteDataSource
import com.peter.iwanttobelieve.data.datasource.UserAuthDataSource
import com.peter.iwanttobelieve.data.datasource.UserRemoteDataSource
import com.peter.iwanttobelieve.data.repository.PostRepository
import com.peter.iwanttobelieve.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        authDataSource: UserAuthDataSource,
        remoteDataSource: UserRemoteDataSource
    ): UserRepository {
        return UserRepository(authDataSource, remoteDataSource)
    }

    @Provides
    @Singleton
    fun providePostRepository(
        postDataSource: PostRemoteDataSource,
        authDataSource: UserAuthDataSource,
        userDataSource: UserRemoteDataSource
    ): PostRepository {
        return PostRepository(postDataSource, authDataSource, userDataSource)
    }
}
