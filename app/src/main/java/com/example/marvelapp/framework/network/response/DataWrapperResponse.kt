package com.example.marvelapp.framework.network.response

data class DataWrapperResponse(
    val code: Int,
    val status: String,
    val copyright: String,
    val etag: String,
    val data: DataContainerResponse
)
