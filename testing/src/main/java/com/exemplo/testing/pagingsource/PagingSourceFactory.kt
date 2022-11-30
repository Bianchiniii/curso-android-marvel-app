package com.exemplo.testing.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bianchini.vinicius.matheus.core.domain.model.Character

class PagingSourceFactory {

    fun create(heroes: List<Character>): PagingSource<Int, Character> =
        object : PagingSource<Int, Character>() {
            override fun getRefreshKey(state: PagingState<Int, Character>): Int? = 0

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
                return LoadResult.Page(
                    data = heroes,
                    prevKey = null,
                    nextKey = 20
                )
            }

        }
}