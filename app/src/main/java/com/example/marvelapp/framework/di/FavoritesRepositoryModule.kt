package com.example.marvelapp.framework.di

import com.bianchini.vinicius.matheus.core.data.repository.favorite.FavoritesLocalDataSource
import com.bianchini.vinicius.matheus.core.data.repository.favorite.FavoritesRepository
import com.example.marvelapp.framework.FavoriteRepositoryImpl
import com.example.marvelapp.framework.local.RoomFavoritesDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface FavoritesRepositoryModule {

    @Binds
    fun bindsRoomFavoritesDataSource(impl: FavoriteRepositoryImpl): FavoritesRepository

    @Binds
    fun bindsFavoritesLocalDataSource(impl: RoomFavoritesDataSource): FavoritesLocalDataSource

}