package com.guagua.githuber_android.model.data.error

import com.google.gson.annotations.SerializedName


sealed class Error {
    data class ThrowableError(val throwable: Throwable) : Error()

    data class GithubApiError(
        @SerializedName("message") val message: String?,
        @SerializedName("documentation_url") val documentationUrl: String?
    ) : Error()

    fun getErrorMessage(): String? {
        return when (this) {
            is ThrowableError -> throwable.message
            is GithubApiError -> message
        }
    }
}