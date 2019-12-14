package com.kkbox.githuber_android.ui

import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.kkbox.githuber_android.model.common.ApiResponse
import com.kkbox.githuber_android.model.common.SingleLiveEvent
import com.kkbox.githuber_android.model.data.User
import com.kkbox.githuber_android.model.user.UserRepository

class SearchListViewModel(private val repository: UserRepository) : ViewModel() {

    private val perPage = 10
    private var currentPage = 1
    private var userList = listOf<User>()

    val state = SingleLiveEvent<SearchListViewState>()

    private val apiResponseObserver = Observer<ApiResponse> { apiResponse ->
        if (apiResponse is ApiResponse.Success<*>) {
            Log.d("SearchListViewModel", "${apiResponse.data}")
            val changeType = if (currentPage == 1) ChangeType.MODIFIED else ChangeType.ADDED
            setState(SearchListViewState.DataSetChanged(changeType))
        } else if (apiResponse is ApiResponse.Failed) {
            setState(SearchListViewState.Alert(apiResponse.error.msg))
        }
    }

    init {
        repository.apiResponse.observeForever(apiResponseObserver)
    }

    fun searchUsers(query: String) {
        setState(SearchListViewState.ListLoading)
        repository.searchUsers(query, currentPage, perPage)
    }

    private fun setState(state: SearchListViewState) {
        this.state.postValue(state)
    }

    override fun onCleared() {
        super.onCleared()
        repository.apiResponse.removeObserver(apiResponseObserver)
    }
}