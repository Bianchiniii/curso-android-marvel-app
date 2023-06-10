package com.example.marvelapp.framework.network

import com.bianchini.vinicius.matheus.core.domain.model.CharacterPaging
import com.example.marvelapp.framework.network.response.DataWrapperResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface MarvelApi {

    @GET("characters")
    suspend fun getCharacters(
        @QueryMap queries: Map<String, String>
    ): DataWrapperResponse
}