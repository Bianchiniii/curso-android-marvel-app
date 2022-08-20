package com.bianchini.vinicius.matheus.core.data.repository

//<T> dessa forma quem implementar deve especificar o tipo de retorno objeto que deseja
interface CharactersRemoteDataSource<T> {
    suspend fun fetchCharacters(queries: Map<String, String>): T
}