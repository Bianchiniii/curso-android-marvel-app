package com.bianchini.vinicius.matheus.core.domain.model

data class CharacterPaging(
    val results: List<Character>,
    val offset: Int,
    val total: Int
)
