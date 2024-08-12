package com.example.marvelapp.framework.di

import com.bianchini.vinicius.matheus.core.data.repository.StorageLocalDataSource
import com.bianchini.vinicius.matheus.core.data.repository.StorageRepository
import com.example.marvelapp.framework.StorageRepositoryImpl
import com.example.marvelapp.framework.local.DataStorePreferencesDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface StorageRepositoryModule {

    @Binds
    fun bindsStorageRepository(impl: StorageRepositoryImpl): StorageRepository

    @Binds
    @Singleton
    fun bindsLocalDataSource(impl: DataStorePreferencesDataSource): StorageLocalDataSource
}