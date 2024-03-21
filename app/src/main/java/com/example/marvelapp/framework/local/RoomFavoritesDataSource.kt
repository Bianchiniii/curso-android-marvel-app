package com.example.marvelapp.framework.local

import com.bianchini.vinicius.matheus.core.data.repository.favorite.FavoritesLocalDataSource
import com.bianchini.vinicius.matheus.core.domain.model.Character
import com.example.marvelapp.framework.db.dao.FavoriteDao
import com.example.marvelapp.framework.db.entity.FavoriteEntity
import com.example.marvelapp.framework.db.entity.toCharactersModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomFavoritesDataSource @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoritesLocalDataSource {

    override fun getAllFavorites(): Flow<List<Character>> {
        return favoriteDao.loadFavorites().map {
            it.toCharactersModel()
        }
    }

    override fun isFavorite(characterId: Int): Boolean {
        return favoriteDao.isFavorite(characterId) != null
    }

    override suspend fun saveFavorite(character: Character) {
        return favoriteDao.insertFavorites(character.toFavoriteEntity())
    }

    override suspend fun deleteFavorite(character: Character) {
        return favoriteDao.removeFavorites(character.toFavoriteEntity())
    }

    private fun Character.toFavoriteEntity() = FavoriteEntity(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl
    )
}
