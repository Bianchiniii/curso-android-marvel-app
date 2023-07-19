package com.example.marvelapp.framework.network.response

import com.bianchini.vinicius.matheus.core.domain.model.Event
import com.google.gson.annotations.SerializedName

data class EventResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("thumbnail")
    val thumbnail: ThumbnailResponse
)

fun EventResponse.toComicModel() = Event(
    id = this.id,
    thumbnail = this.thumbnail.getHttpsUrl()
)
