package com.guagua.githuber_android.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.guagua.githuber_android.model.api.UserService
import com.guagua.githuber_android.model.common.ApiBuilder
import com.guagua.githuber_android.model.user.UserRemoteDataSource
import com.guagua.githuber_android.model.user.UserRepository
import com.guagua.githuber_android.ui.SearchListViewModel
import com.guagua.githuber_android.ui.SearchListViewModelFactory

object InjectUtil {

    private val gson = Gson()

    fun provideSearchListViewModel(fragment: Fragment): SearchListViewModel {
        val factory = SearchListViewModelFactory(provideUserRepository())
        return ViewModelProviders.of(fragment, factory).get(SearchListViewModel::class.java)
    }

    private fun provideUserRepository() = UserRepository(provideUserRemoteDataSource())

    private fun provideUserRemoteDataSource() = UserRemoteDataSource(provideUserService(), gson)

    private fun provideUserService() =
        ApiBuilder(UserService::class.java, "https://api.github.com").build()
}