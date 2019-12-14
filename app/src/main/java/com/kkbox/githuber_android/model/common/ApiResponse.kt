package com.kkbox.githuber_android.model.common

import com.kkbox.githuber_android.model.data.error.Error


sealed class ApiResponse {
    data class Success<T>(val data: T): ApiResponse()
    data class Failed(val error: Error): ApiResponse()

    override fun toString(): String {
        return when(this) {
            is Success<*> -> "Success[data=$data]"
            is Failed -> "Error[error=${error.msg}]"
        }
    }
}