package com.bianchini.vinicius.matheus.core.data.repository

import java.time.temporal.TemporalQueries

interface CharactersRemoteDataSource<T> {
    suspend fun fetchCharacters(queries: Map<String, String>): T
}