package com.example.marvelapp.framework.network.response

import com.bianchini.vinicius.matheus.core.domain.model.Comic
import com.google.gson.annotations.SerializedName

data class ComicResponse(
        @SerializedName("id")
        val id: Int,
        @SerializedName("thumbnail")
        val thumbnail: ThumbnailResponse
)

fun ComicResponse.toComicModel() = Comic(
        id = this.id,
        thumbnail = "${this.thumbnail.path}.${this.thumbnail.extension}".replace("http", "https")
)
