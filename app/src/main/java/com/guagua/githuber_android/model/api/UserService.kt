package com.guagua.githuber_android.model.api

import com.guagua.githuber_android.model.data.Collection
import com.guagua.githuber_android.model.data.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface UserService {

    @Headers("accept: application/json")
    @GET("/search/users")
    fun searchUsers(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Call<Collection<User>>
}