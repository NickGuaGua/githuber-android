package com.guagua.githuber_android.domain

import com.guagua.githuber_android.model.data.User

fun User.toUserItem(): UserItem {
    return UserItem(id = id, name = login, avatar = avatarUrl)
}