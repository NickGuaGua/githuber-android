package com.kkbox.githuber_android.model.api

import com.kkbox.githuber_android.model.data.User
import retrofit2.Call
import retrofit2.http.*
import com.kkbox.githuber_android.model.data.Collection

interface UserService {

    @Headers("accept: application/json")
    @GET("/search/users")
    fun searchUsers(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Call<Collection<User>>
}