package com.example.marvelapp.framework.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bianchini.vinicius.matheus.core.data.repository.CharactersRemoteDataSource
import com.bianchini.vinicius.matheus.core.domain.model.Character

class CharactersPagingSource(
    private val remoteDataSource: CharactersRemoteDataSource,
    private val query: String
) : PagingSource<Int, Character>() {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {
            val offset = params.key ?: 0

            val queries = hashMapOf(OFFSET to offset.toString())

            if (query.isNotEmpty()) queries[NAME_STARTS_WITH] = query

            val response = remoteDataSource.fetchCharacters(queries)

            val responseOffSet = response.offset
            val responseTotal = response.total

            LoadResult.Page(
                data = response.results,
                prevKey = null,
                nextKey = if (responseOffSet < responseTotal) responseOffSet + LIMIT else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        //executado quando precisar invalidar o adapter, ex :swipe, etc ou quando acontecer um process death
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(LIMIT) ?: anchorPage?.nextKey?.minus(LIMIT)
        }
    }

    companion object {
        private const val OFFSET = "offset"
        private const val NAME_STARTS_WITH = "nameStartsWith"
        private const val LIMIT = 20
    }
}