package com.guagua.githuber_android.ui

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.guagua.githuber_android.domain.UserItem
import com.guagua.githuber_android.domain.toUserItem
import com.guagua.githuber_android.model.common.ApiResponse
import com.guagua.githuber_android.model.common.SingleLiveEvent
import com.guagua.githuber_android.model.data.Collection
import com.guagua.githuber_android.model.data.User
import com.guagua.githuber_android.model.user.UserRepository

class SearchListViewModel(private val repository: UserRepository) : ViewModel() {

    private val perPage = 10
    private var currentPage = 1
    private var currentQuery: String = ""

    val state = SingleLiveEvent<SearchListViewState>()

    val onLoadMoreListener: () -> Unit = {
        currentPage += 1
        searchUsers(currentQuery, currentPage)
    }

    private val apiResponseObserver = Observer<ApiResponse> { apiResponse ->
        if (apiResponse is ApiResponse.Success<*>) {
            if (apiResponse.data !is Collection<*>) return@Observer

            val items = apiResponse.data.items.filterIsInstance<User>().map { it.toUserItem() }
            val collection: Collection<UserItem> = Collection(apiResponse.data.totalCount, items)
            val changeType = if (currentPage == 1) ChangeType.MODIFIED else ChangeType.ADDED

            setState(SearchListViewState.DataSetChanged(collection, changeType))
        } else if (apiResponse is ApiResponse.Failed) {
            setState(
                SearchListViewState.Alert(
                    apiResponse.error.getErrorMessage() ?: "Something went wrong."
                )
            )
        }
    }

    init {
        repository.apiResponse.observeForever(apiResponseObserver)
    }

    fun searchUsers(query: String, page: Int = 1) {
        if (query.isBlank()) return

        if (page == 1) {
            setState(SearchListViewState.ListLoading)
        }

        currentPage = page
        currentQuery = query
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