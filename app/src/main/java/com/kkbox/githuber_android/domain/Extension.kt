package com.kkbox.githuber_android.domain

import com.kkbox.githuber_android.model.data.User

fun User.toUserItem(): UserItem {
    return UserItem(id = id, name = login, avatar = avatarUrl)
}