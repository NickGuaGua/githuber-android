package com.kkbox.githuber_android.ui

sealed class SearchListViewState {
    object ListLoading : SearchListViewState()
    data class DataSetChanged(val changeType: ChangeType) : SearchListViewState()
    data class Alert(val message: String) : SearchListViewState()
}