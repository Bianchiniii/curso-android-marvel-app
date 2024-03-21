package com.bianchini.vinicius.matheus.core.data.repository.favorite

import com.bianchini.vinicius.matheus.core.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface FavoritesLocalDataSource {

    fun getAllFavorites(): Flow<List<Character>>

    fun isFavorite(characterId: Int): Boolean

    suspend fun saveFavorite(character: Character)

    suspend fun deleteFavorite(character: Character)
}