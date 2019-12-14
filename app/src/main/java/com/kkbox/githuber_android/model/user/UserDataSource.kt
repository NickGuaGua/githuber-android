package com.kkbox.githuber_android.model.user

import com.kkbox.githuber_android.model.common.ApiResponse
import com.kkbox.githuber_android.model.common.SingleLiveEvent

interface UserDataSource {
    val apiResponse: SingleLiveEvent<ApiResponse>

    fun searchUsers(query: String, page: Int, perPage: Int)
}