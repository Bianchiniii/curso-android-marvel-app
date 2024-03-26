package com.example.marvelapp.presentation.commun

interface ListItem {

    val key: Long

    fun areItemsTheSame(other: ListItem) = this.key == other.key

    fun areContentTheSame(other: ListItem) = this == other
}