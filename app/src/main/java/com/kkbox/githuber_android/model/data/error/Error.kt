package com.kkbox.githuber_android.model.data.error

import com.google.gson.annotations.SerializedName


sealed class Error(val msg: String) {
    data class ThrowableError(val throwable: Throwable): Error(throwable.message ?: "")

    data class GithubApiError(
        @SerializedName("message") val message: String,
        @SerializedName("documentation_url") val documentationUrl: String
    ): Error(message)
}