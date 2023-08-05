package com.example.marvelapp.framework

import androidx.paging.PagingSource
import com.bianchini.vinicius.matheus.core.data.repository.character.CharactersRemoteDataSource
import com.bianchini.vinicius.matheus.core.data.repository.character.CharactersRepository
import com.bianchini.vinicius.matheus.core.domain.model.Character
import com.bianchini.vinicius.matheus.core.domain.model.Comic
import com.bianchini.vinicius.matheus.core.domain.model.Event
import com.example.marvelapp.framework.paging.CharactersPagingSource
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val remoteDataSource: CharactersRemoteDataSource
) : CharactersRepository {

    override fun getCharacters(query: String): PagingSource<Int, Character> {
        return CharactersPagingSource(remoteDataSource, query)
    }

    override suspend fun getComics(characterId: Int): List<Comic> {
        return remoteDataSource.fetchComics(characterId)
    }

    override suspend fun getEvents(characterId: Int): List<Event> {
        return remoteDataSource.fetchEvent(characterId)
    }
}