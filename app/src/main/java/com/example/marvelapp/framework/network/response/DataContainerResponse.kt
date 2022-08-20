package com.example.marvelapp.framework.network.response

data class DataContainerResponse(
    val results: List<CharacterResponse>,
    val offset: Int,
    val total: Int
)
