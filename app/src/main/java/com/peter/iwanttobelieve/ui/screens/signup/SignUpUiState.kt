package com.peter.iwanttobelieve.ui.screens.signup

import com.peter.iwanttobelieve.util.ErrorType

data class SignUpUiState (
    val isLoading : Boolean = false,
    val isSuccess : Boolean = false,
    val errorMessage : ErrorType? = null
)