package com.example.marvelapp.framework.network.response

import com.google.gson.annotations.SerializedName

data class DataContainerResponse<T>(
    @SerializedName("results")
    val results: List<T>,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("total")
    val total: Int
)
