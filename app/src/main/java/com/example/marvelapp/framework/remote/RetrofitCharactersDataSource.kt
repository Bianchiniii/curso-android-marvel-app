package com.example.marvelapp.framework.remote

import com.bianchini.vinicius.matheus.core.data.repository.CharactersRemoteDataSource
import com.bianchini.vinicius.matheus.core.domain.model.CharacterPaging
import com.example.marvelapp.framework.network.MarvelApi
import com.example.marvelapp.framework.network.response.toCharacterModel
import javax.inject.Inject

class RetrofitCharactersDataSource @Inject constructor(
    private val marvelApi: MarvelApi
) : CharactersRemoteDataSource {

    override suspend fun fetchCharacters(queries: Map<String, String>): CharacterPaging {
        val data = marvelApi.getCharacters(queries)
        val characters = data.data.results.map {
            it.toCharacterModel()
        }

        return CharacterPaging(
            results = characters,
            offset = data.data.offset,
            total = data.data.total
        )
    }
}