package com.bianchini.vinicius.matheus.core.data.response

data class DataWrapperResponse(
    val code: Int,
    val status: String,
    val copyright: String,
    val etag: String,
    val data: DataContainerResponse
)
