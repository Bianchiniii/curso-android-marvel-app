package com.bianchini.vinicius.matheus.core.data.repository

import androidx.paging.PagingSource
import com.bianchini.vinicius.matheus.core.domain.model.Character

interface CharactersRepository {
    fun getCharacters(query: String): PagingSource<Int, Character>
}