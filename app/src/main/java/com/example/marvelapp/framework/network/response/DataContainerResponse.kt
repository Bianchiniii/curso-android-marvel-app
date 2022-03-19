package com.example.marvelapp.framework.network.response

import com.bianchini.vinicius.matheus.core.domain.model.Character

data class DataContainerResponse(
    val results: List<CharacterResponse>,
    val offset: Int,
    val total: Int
)
