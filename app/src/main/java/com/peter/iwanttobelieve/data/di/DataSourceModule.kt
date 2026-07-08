package com.peter.iwanttobelieve.data.di

import com.peter.iwanttobelieve.data.datasource.PostRemoteDataSource
import com.peter.iwanttobelieve.data.datasource.UserAuthDataSource
import com.peter.iwanttobelieve.data.datasource.UserRemoteDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideUserAuthDataSource(auth: FirebaseAuth): UserAuthDataSource {
        return UserAuthDataSource(auth)
    }

    @Provides
    @Singleton
    fun provideUserRemoteDataSource(
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ): UserRemoteDataSource {
        return UserRemoteDataSource(firestore, storage)
    }

    @Provides
    @Singleton
    fun providePostRemoteDataSource(
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ): PostRemoteDataSource {
        return PostRemoteDataSource(firestore, storage)
    }
}
