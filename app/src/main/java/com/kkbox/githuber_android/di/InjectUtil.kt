package com.kkbox.githuber_android.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.gson.GsonBuilder
import com.kkbox.githuber_android.model.api.UserService
import com.kkbox.githuber_android.model.common.ApiBuilder
import com.kkbox.githuber_android.model.user.UserRemoteDataSource
import com.kkbox.githuber_android.model.user.UserRepository
import com.kkbox.githuber_android.ui.SearchListViewModel
import com.kkbox.githuber_android.ui.SearchListViewModelFactory

object InjectUtil {

    private val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

    fun provideSearchListViewModel(fragment: Fragment): SearchListViewModel {
        val factory = SearchListViewModelFactory(provideUserRepository())
        return ViewModelProviders.of(fragment, factory).get(SearchListViewModel::class.java)
    }

    private fun provideUserRepository() = UserRepository(provideUserRemoteDataSource())

    private fun provideUserRemoteDataSource() = UserRemoteDataSource(provideUserService(), gson)

    private fun provideUserService() =
        ApiBuilder(UserService::class.java, "https://api.github.com").build()
}