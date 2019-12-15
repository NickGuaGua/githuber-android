package com.guagua.githuber_android.model.user

import com.guagua.githuber_android.model.common.ApiResponse
import com.guagua.githuber_android.model.common.SingleLiveEvent

interface UserDataSource {
    val apiResponse: SingleLiveEvent<ApiResponse>

    fun searchUsers(query: String, page: Int, perPage: Int)
}