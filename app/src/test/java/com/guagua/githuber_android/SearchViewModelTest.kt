package com.guagua.githuber_android

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.guagua.githuber_android.model.user.UserRepository
import com.guagua.githuber_android.ui.SearchListViewModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var repository: UserRepository

    private lateinit var viewModel: SearchListViewModel

    @Before
    fun init() {
        MockKAnnotations.init(this, relaxed = true)
        viewModel = SearchListViewModel(repository)
    }

    @Test
    fun `test search and get result`() {
        // GIVEN
        val query = "test"

        // WHEN
        viewModel.searchUsers(query)

        // THEN
        verify(exactly = 1) { repository.searchUsers(query, 1, 10) }
    }

    @Test
    fun `test search with blank query`() {
        // GIVEN
        val query = "   "

        // WHEN
        viewModel.searchUsers(query)

        // THEN
        verify(exactly = 0) { repository.searchUsers(query, 1, 10) }
    }
}