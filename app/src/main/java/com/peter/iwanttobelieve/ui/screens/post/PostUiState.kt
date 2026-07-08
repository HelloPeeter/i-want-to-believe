package com.peter.iwanttobelieve.ui.screens.post

import com.peter.iwanttobelieve.data.model.User
import com.peter.iwanttobelieve.util.ErrorType

data class PostUiState (
    val user : User? = null,
    val isLoading : Boolean = false,
    val isSuccess : Boolean = false,
    val errorMessage : ErrorType? = null
)