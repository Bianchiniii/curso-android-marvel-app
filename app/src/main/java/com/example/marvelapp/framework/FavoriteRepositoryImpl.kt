package com.example.marvelapp.framework

import com.bianchini.vinicius.matheus.core.data.repository.favorite.FavoritesLocalDataSource
import com.bianchini.vinicius.matheus.core.data.repository.favorite.FavoritesRepository
import com.bianchini.vinicius.matheus.core.domain.model.Character
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoritesLocalDataSource: FavoritesLocalDataSource
) : FavoritesRepository {

    override fun getAllFavorites(): Flow<List<Character>> {
        return favoritesLocalDataSource.getAllFavorites()
    }

    override fun isFavorite(characterId: Int): Boolean {
        return favoritesLocalDataSource.isFavorite(characterId)
    }

    override suspend fun saveFavorite(character: Character) {
        return favoritesLocalDataSource.saveFavorite(character)
    }

    override suspend fun deleteFavorite(character: Character) {
        return favoritesLocalDataSource.deleteFavorite(character)
    }
}