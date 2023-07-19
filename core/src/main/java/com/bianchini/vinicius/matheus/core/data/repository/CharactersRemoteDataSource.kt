package com.bianchini.vinicius.matheus.core.data.repository

import com.bianchini.vinicius.matheus.core.domain.model.CharacterPaging
import com.bianchini.vinicius.matheus.core.domain.model.Comic
import com.bianchini.vinicius.matheus.core.domain.model.Event

interface CharactersRemoteDataSource {
    suspend fun fetchCharacters(queries: Map<String, String>): CharacterPaging

    suspend fun fetchComics(characterId: Int): List<Comic>

    suspend fun fetchEvent(characterId: Int): List<Event>
}