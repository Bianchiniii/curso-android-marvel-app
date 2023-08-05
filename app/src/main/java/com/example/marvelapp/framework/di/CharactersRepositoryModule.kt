package com.example.marvelapp.framework.di

import com.bianchini.vinicius.matheus.core.data.repository.character.CharactersRemoteDataSource
import com.bianchini.vinicius.matheus.core.data.repository.character.CharactersRepository
import com.example.marvelapp.framework.CharactersRepositoryImpl
import com.example.marvelapp.framework.remote.RetrofitCharactersDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CharactersRepositoryModule {
    @Binds
    fun bindCharacterRepository(repository: CharactersRepositoryImpl): CharactersRepository

    @Binds
    fun bindRemoteDataSource(
        dataSource: RetrofitCharactersDataSource
    ): CharactersRemoteDataSource
}