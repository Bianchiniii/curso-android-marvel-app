package com.example.marvelapp.presentation.details

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

// @Keep para não ofuscar a classe
@Keep
@Parcelize
data class DetailViewArg(
    val id: Int,
    val name: String,
    val imageUrl: String,
) : Parcelable
