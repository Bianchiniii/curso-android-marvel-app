package com.example.marvelapp.framework.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.APPEND
import androidx.paging.LoadType.PREPEND
import androidx.paging.LoadType.REFRESH
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.bianchini.vinicius.matheus.core.data.repository.character.CharactersRemoteDataSource
import com.example.marvelapp.framework.db.AppDatabase
import com.example.marvelapp.framework.db.entity.CharacterEntity
import com.example.marvelapp.framework.db.entity.RemoteKeyEntity
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator @Inject constructor(
    private val query: String,
    private val appDatabase: AppDatabase,
    private val remoteDataSource: CharactersRemoteDataSource
) : RemoteMediator<Int, CharacterEntity>() {

    private val characterDao = appDatabase.characterDao()
    private val remoteKeyDao = appDatabase.remoteKeyDao()

    @Suppress("ReturnCount")
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        return try {
            val offset = when (loadType) {
                REFRESH -> INITIAL
                PREPEND -> {
                    return MediatorResult.Success(true)
                }

                APPEND -> {
                    val remoteKey = appDatabase.withTransaction {
                        remoteKeyDao.remoteKey()
                    }

                    if (remoteKey.nextOffset == null) {
                        return MediatorResult.Success(true)
                    }

                    remoteKey.nextOffset
                }
            }

            val queries = hashMapOf(OFFSET to offset.toString())

            if (query.isNotEmpty()) queries[NAME_STARTS_WITH] = query

            val response = remoteDataSource.fetchCharacters(queries)
            val responseOffSet = response.offset
            val responseTotal = response.total

            appDatabase.withTransaction {
                if (loadType == REFRESH) {
                    remoteKeyDao.clearAll()
                    characterDao.clearAll()
                }

                remoteKeyDao.insertOrReplace(
                    RemoteKeyEntity(nextOffset = responseOffSet + state.config.pageSize)
                )

                val charactersEntities = response.results.map {
                    CharacterEntity(
                        id = it.id,
                        name = it.name,
                        imageUrl = it.imageUrl
                    )
                }

                characterDao.insertAll(charactersEntities)
            }

            MediatorResult.Success(responseOffSet >= responseTotal)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    companion object {
        private const val OFFSET = "offset"
        private const val NAME_STARTS_WITH = "nameStartsWith"
        private const val INITIAL = 0
    }
}