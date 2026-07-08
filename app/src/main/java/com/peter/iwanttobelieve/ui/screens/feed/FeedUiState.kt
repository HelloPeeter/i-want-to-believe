package com.peter.iwanttobelieve.ui.screens.feed

import com.peter.iwanttobelieve.ui.model.PostWithAuthor
import com.peter.iwanttobelieve.util.ErrorType

data class FeedUiState (

    val postsWithAuthor : List<PostWithAuthor> = emptyList(),
    val isLoading : Boolean = false,
    val isSuccess : Boolean = false,
    val errorMessage : ErrorType? = null,
    val currentUserId : String? = null
)