package com.peter.iwanttobelieve.ui.model

import com.peter.iwanttobelieve.data.model.Post
import com.peter.iwanttobelieve.data.model.User

data class PostWithAuthor (
    val post: Post,
    val author: User?
)