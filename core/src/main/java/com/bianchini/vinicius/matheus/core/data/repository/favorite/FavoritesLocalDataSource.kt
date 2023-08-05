package com.bianchini.vinicius.matheus.core.data.repository.favorite

import com.bianchini.vinicius.matheus.core.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface FavoritesLocalDataSource {

    suspend fun getAllFavorites(): Flow<List<Character>>

    suspend fun saveFavorite(character: Character)

    suspend fun deleteFavorite(character: Character)
}