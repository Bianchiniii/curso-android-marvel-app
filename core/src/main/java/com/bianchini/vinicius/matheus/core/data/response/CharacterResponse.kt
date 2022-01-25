package com.bianchini.vinicius.matheus.core.data.response

data class CharacterResponse(
    val id: String,
    val name: String,
    val description: String,
    val modified: String,
    val thumbnail: ThumbnailResponse
)
