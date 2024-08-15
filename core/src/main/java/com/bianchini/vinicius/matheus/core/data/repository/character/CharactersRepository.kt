package com.bianchini.vinicius.matheus.core.data.repository.character

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bianchini.vinicius.matheus.core.domain.model.Character
import com.bianchini.vinicius.matheus.core.domain.model.Comic
import com.bianchini.vinicius.matheus.core.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {

    fun getCachedCharacters(
        query: String,
        orderBy: String,
        pagingConfig: PagingConfig
    ): Flow<PagingData<Character>>

    suspend fun getComics(characterId: Int): List<Comic>

    suspend fun getEvents(characterId: Int): List<Event>
}