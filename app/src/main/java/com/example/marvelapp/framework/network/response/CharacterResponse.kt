package com.example.marvelapp.framework.network.response

data class CharacterResponse(
    val id: String,
    val name: String,
    val description: String,
    val modified: String,
    val thumbnail: ThumbnailResponse
)
