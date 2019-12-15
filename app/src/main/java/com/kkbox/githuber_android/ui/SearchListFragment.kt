package com.kkbox.githuber_android.ui

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kkbox.githuber_android.R
import com.kkbox.githuber_android.di.InjectUtil
import kotlinx.android.synthetic.main.fragment_search_list.*
import kotlinx.android.synthetic.main.view_loading.*


class SearchListFragment : Fragment() {

    companion object {
        fun newInstance(): SearchListFragment = SearchListFragment()
    }

    private lateinit var viewModel: SearchListViewModel

    private val searchAdapter: SearchListAdapter = SearchListAdapter()

    private val stateObserver = Observer<SearchListViewState> { state ->
        state ?: return@Observer

        when (state) {
            is SearchListViewState.ListLoading -> {
                onListLoading()
            }
            is SearchListViewState.DataSetChanged -> {
                onListLoaded()
                with(state.collection) {
                    searchAdapter.maxDataCount = totalCount

                    when (state.changeType) {
                        ChangeType.ADDED -> {
                            searchAdapter.addItems(items)
                        }
                        ChangeType.MODIFIED -> {
                            searchAdapter.setItems(items)
                        }
                    }
                }
            }
            is SearchListViewState.Alert -> {
                onListLoaded()
                showAlert(state.message)
                searchAdapter.cancelLoadMore()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = InjectUtil.provideSearchListViewModel(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchUsers(s.toString())
            }
        })

        searchRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = searchAdapter.also {
                it.onLoadMoreListener = viewModel.onLoadMoreListener
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.state.observe(this, stateObserver)
    }

    private fun onListLoading() {
        loadingView.visibility = View.VISIBLE
    }

    private fun onListLoaded() {
        loadingView.visibility = View.GONE
    }

    private fun showAlert(message: String) {
        AlertDialog.Builder(context).apply {
            setMessage(message)
            setPositiveButton(context.getString(R.string.ok)) { dialog, _ -> }
        }.show()
    }
}