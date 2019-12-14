package com.kkbox.githuber_android.model.user

import android.util.Log
import com.google.gson.Gson
import com.kkbox.githuber_android.model.data.User
import com.kkbox.githuber_android.model.api.UserService
import com.kkbox.githuber_android.model.common.ApiResponse
import com.kkbox.githuber_android.model.common.SingleLiveEvent
import com.kkbox.githuber_android.model.data.Collection
import com.kkbox.githuber_android.model.data.error.Error
import retrofit2.Call
import retrofit2.Response

class UserRemoteDataSource(private val userService: UserService, private val gson: Gson): UserDataSource {
    override val apiResponse: SingleLiveEvent<ApiResponse> = SingleLiveEvent()

    private var searchUserCall: Call<Collection<User>>? = null

    override fun searchUsers(query: String, page: Int, perPage: Int) {
        searchUserCall?.let {
            if (it.isExecuted && !it.isCanceled) {
                it.cancel()
            }
        }

        userService.searchUsers(query, page, perPage).also {
            searchUserCall = it
            it.enqueue(getRetrofit2Callback<Collection<User>>())
        }
    }

    private fun <ResultType> getRetrofit2Callback() = object : retrofit2.Callback<ResultType>{
        override fun onFailure(call: Call<ResultType>, t: Throwable) {
            if (!call.isCanceled) {
                // Failure but not cause by cancelling the request.
                setApiResponse(ApiResponse.Failed(Error.ThrowableError(t)))
            }
        }

        override fun onResponse(call: Call<ResultType>, response: Response<ResultType>?) {
            response?.body()?.let {
                setApiResponse(ApiResponse.Success(it))
                return
            }

            response?.errorBody()?.let {
                val errorString = it.string()
                val error = gson.fromJson(errorString, Error.GithubApiError::class.java)
                setApiResponse(ApiResponse.Failed(error))
            }
        }
    }

    private fun setApiResponse(response: ApiResponse) {
        Log.d(this::class.java.simpleName, "$response")
        apiResponse.postValue(response)
    }
}