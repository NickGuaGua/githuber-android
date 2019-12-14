package com.kkbox.githuber_android.model.data

import com.google.gson.annotations.SerializedName

data class Collection<T>(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("items") val items: List<T>
)