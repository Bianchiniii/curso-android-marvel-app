package com.example.marvelapp.framework.network.response

import com.google.gson.annotations.SerializedName

data class ThumbnailResponse(
    @SerializedName("path")
    val path: String,
    @SerializedName("extension")
    val extension: String
)

fun ThumbnailResponse.getHttpsUrl(): String {
    return "${path}.${extension}".replace("http", "https")
}