package com.example.marvelapp.framework.remote

import com.bianchini.vinicius.matheus.core.data.repository.CharactersRemoteDataSource
import com.bianchini.vinicius.matheus.core.domain.model.CharacterPaging
import com.bianchini.vinicius.matheus.core.domain.model.Comic
import com.bianchini.vinicius.matheus.core.domain.model.Event
import com.example.marvelapp.framework.network.MarvelApi
import com.example.marvelapp.framework.network.response.toCharacterModel
import com.example.marvelapp.framework.network.response.toComicModel
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

    override suspend fun fetchComics(characterId: Int): List<Comic> {
        return marvelApi.getCharacterComics(characterId).data.results.map {
            it.toComicModel()
        }
    }

    override suspend fun fetchEvent(characterId: Int): List<Event> {
        return marvelApi.getCharacterEvents(characterId).data.results.map {
            it.toComicModel()
        }
    }
}