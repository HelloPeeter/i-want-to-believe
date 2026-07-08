package com.peter.iwanttobelieve.ui.screens.signin

import com.peter.iwanttobelieve.util.ErrorType

data class SignInUiState (
    val isLoading : Boolean = false,
    val isSuccess : Boolean = false,
    val errorMessage : ErrorType? = null
)