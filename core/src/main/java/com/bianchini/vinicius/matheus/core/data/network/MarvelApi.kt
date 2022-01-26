package com.bianchini.vinicius.matheus.core.data.network

import com.bianchini.vinicius.matheus.core.data.response.DataWrapperResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface MarvelApi {

    @GET("characters")
    suspend fun getCharacters(
        @QueryMap
        queries: Map<String, String>
    ): DataWrapperResponse
}