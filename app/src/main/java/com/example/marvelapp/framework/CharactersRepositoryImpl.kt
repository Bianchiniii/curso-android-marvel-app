package com.example.marvelapp.framework

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.bianchini.vinicius.matheus.core.data.repository.character.CharactersRemoteDataSource
import com.bianchini.vinicius.matheus.core.data.repository.character.CharactersRepository
import com.bianchini.vinicius.matheus.core.domain.model.Character
import com.bianchini.vinicius.matheus.core.domain.model.Comic
import com.bianchini.vinicius.matheus.core.domain.model.Event
import com.example.marvelapp.framework.db.AppDatabase
import com.example.marvelapp.framework.paging.CharacterRemoteMediator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CharactersRepositoryImpl @Inject constructor(
    private val remoteDataSource: CharactersRemoteDataSource,
    private val database: AppDatabase,
) : CharactersRepository {

    override fun getCachedCharacters(
        query: String,
        pagingConfig: PagingConfig
    ): Flow<PagingData<Character>> {
        return Pager(
            pagingConfig,
            remoteMediator = CharacterRemoteMediator(
                query,
                database,
                remoteDataSource
            )
        ) {
            database.characterDao().pagingSource()
        }.flow.map { pagingData ->
            pagingData.map {
                Character(
                    it.id,
                    it.name,
                    it.imageUrl
                )
            }
        }
    }

    override suspend fun getComics(characterId: Int): List<Comic> {
        return remoteDataSource.fetchComics(characterId)
    }

    override suspend fun getEvents(characterId: Int): List<Event> {
        return remoteDataSource.fetchEvent(characterId)
    }
}