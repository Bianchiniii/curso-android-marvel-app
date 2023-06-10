package com.bianchini.vinicius.matheus.core.data.repository

import com.bianchini.vinicius.matheus.core.domain.model.CharacterPaging

interface CharactersRemoteDataSource {
    suspend fun fetchCharacters(queries: Map<String, String>): CharacterPaging
}