package com.guagua.githuber_android.ui

import com.guagua.githuber_android.domain.UserItem
import com.guagua.githuber_android.model.data.Collection

sealed class SearchListViewState {
    object ListLoading : SearchListViewState()
    data class DataSetChanged(val collection: Collection<UserItem>, val changeType: ChangeType) :
        SearchListViewState()
    data class Alert(val message: String) : SearchListViewState()
}