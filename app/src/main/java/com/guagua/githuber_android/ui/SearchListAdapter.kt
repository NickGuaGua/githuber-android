package com.guagua.githuber_android.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.guagua.githuber_android.R
import com.guagua.githuber_android.domain.UserItem

class SearchListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected val VIEW_TYPE_LOADING = 0x01
    protected val VIEW_TYPE_RETRY = 0x02
    protected val VIEW_TYPE_USER_ITEM = 0x03

    var showLoadMore = true
    var maxDataCount: Int = 0
    var onLoadMoreListener: (() -> Unit)? = null
    val items: MutableList<UserItem> = mutableListOf()

    private var loadMoreState = LoadMoreState.Loading

    enum class LoadMoreState {
        Loading, Retry
    }

    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        fun getView(@LayoutRes id: Int): View {
            return LayoutInflater.from(parent.context).inflate(id, parent, false)
        }

        return when (viewType) {
            VIEW_TYPE_LOADING -> LoadMoreViewHolder(getView(R.layout.listitem_loading))
            VIEW_TYPE_RETRY -> RetryLoadMoreViewHolder(getView(R.layout.listitem_retry))
            else -> UserItemViewHolder(getView(R.layout.listitem_user))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LoadMoreViewHolder -> {
                onLoadMoreListener?.invoke()
            }
            is RetryLoadMoreViewHolder -> {
                holder.itemView.setOnClickListener {
                    updateLoadMoreState(LoadMoreState.Loading)
                }
            }
            is UserItemViewHolder -> {
                with(items[position]) {
                    Glide.with(holder.itemView.context).load(avatar).into(holder.avatar)
                    holder.name.text = name
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (showLoadMore) {
            if (position == items.size && position < maxDataCount) {
                return if (loadMoreState == LoadMoreState.Loading) {
                    VIEW_TYPE_LOADING
                } else {
                    VIEW_TYPE_RETRY
                }
            }
        }

        return VIEW_TYPE_USER_ITEM
    }

    override fun getItemCount(): Int {
        if (showLoadMore) {
            if (items.size < maxDataCount) {
                // Add extra view to show the footer view
                return items.size + 1
            }
        }

        return items.size
    }

    fun setItems(items: List<UserItem>) {
        this.items.apply {
            clear()
            addAll(items)
            notifyDataSetChanged()
        }
    }

    fun addItems(items: List<UserItem>) {
        this.items.addAll(this.items.size, items)
        notifyItemInserted(this.items.size)
    }

    fun cancelLoadMore() {
        updateLoadMoreState(LoadMoreState.Retry)
    }

    private fun updateLoadMoreState(state: LoadMoreState) {
        if (showLoadMore && items.size < maxDataCount) {
            loadMoreState = state
            notifyItemChanged(items.size)
        }
    }

    class LoadMoreViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class RetryLoadMoreViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class UserItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatar: ImageView = view.findViewById(R.id.userAvatar)
        val name: TextView = view.findViewById(R.id.userName)
    }
}