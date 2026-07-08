package com.peter.iwanttobelieve.ui.screens.signup

import androidx.lifecycle.ViewModel
import com.peter.iwanttobelieve.data.repository.UserRepository
import com.peter.iwanttobelieve.util.ErrorMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val errorMapper: ErrorMapper
): ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onSignUpClick(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, isSuccess = false, errorMessage = null)

                val result = userRepository.createUser(email, name, password)

                if(result.isSuccess) {
                    _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)

                    _navigationEvent.emit(NavigationEvent.NavigateToFeed)
                } else {
                    val errorType = errorMapper.map(result.exceptionOrNull())
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = errorType)
                }
            } catch (e: Exception) {
                val errorType = errorMapper.map(e)
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = errorType)       }
        }
    }

    fun onSignInNavigationRequested() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateToSignIn)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    sealed class NavigationEvent {
        object NavigateToFeed : NavigationEvent()
        object NavigateToSignIn : NavigationEvent()
    }
}