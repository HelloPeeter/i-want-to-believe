package com.peter.iwanttobelieve.ui.screens.profile

import com.peter.iwanttobelieve.data.model.User
import com.peter.iwanttobelieve.util.ErrorType

data class ProfileUiState (
    val user: User? = null,
    val isLoading : Boolean = false,
    val isSuccess : Boolean = false,
    val errorMessage : ErrorType? = null,
    val successMessage : String? = null
)