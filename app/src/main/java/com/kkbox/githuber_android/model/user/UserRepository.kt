package com.kkbox.githuber_android.model.user


class UserRepository(private val dataSource: UserDataSource) {
    val apiResponse = dataSource.apiResponse

    fun searchUsers(query: String, page: Int, perPage: Int) {
        dataSource.searchUsers(query, page, perPage)
    }
}