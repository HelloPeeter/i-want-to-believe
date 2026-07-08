package com.peter.iwanttobelieve.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.iwanttobelieve.data.repository.UserRepository
import com.peter.iwanttobelieve.util.ErrorMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor (
    private val userRepository: UserRepository,
    private val errorMapper: ErrorMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        getCurrentUser()
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, isSuccess = false, errorMessage = null)
                val result = userRepository.getCurrentUser()

                if (result.isSuccess) {
                    val user = result.getOrNull()
                    _uiState.value = _uiState.value.copy(user = user, isLoading = false, isSuccess = true)
                } else {
                    val errorType = errorMapper.map(result.exceptionOrNull())
                    _uiState.value = _uiState.value.copy(errorMessage = errorType, isLoading = false)
                }
            } catch (e: Exception) {
                val errorType = errorMapper.map(e)
                _uiState.value = _uiState.value.copy(errorMessage = errorType, isLoading = false)
            }
        }
    }

    fun setUserImage(image: ByteArray) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, isSuccess = false, errorMessage = null)

                val result = userRepository.setUserImage(image)

                if (result.isSuccess) {
                    val resultCurrentUser = userRepository.getCurrentUser()

                    if (resultCurrentUser.isSuccess) {
                        val user = resultCurrentUser.getOrNull()
                        val successMessage = "Imagem de perfil atualizada com sucesso"

                        _uiState.value = _uiState.value.copy(user = user, isLoading = false, isSuccess = true, successMessage = successMessage)
                    } else {
                        val errorType = errorMapper.map(resultCurrentUser.exceptionOrNull())

                        _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = errorType)
                    }
                } else {
                    val errorType = errorMapper.map(result.exceptionOrNull())

                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = errorType)
                }
            } catch (e: Exception) {
                val errorType = errorMapper.map(e)
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = errorType)
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, isSuccess = false, errorMessage = null)

                val result = userRepository.signOut()

                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)

                    _navigationEvent.emit(NavigationEvent.SignOutSuccess)

                } else {
                    val errorType = errorMapper.map(result.exceptionOrNull())

                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = errorType)
                }
            } catch (e: Exception) {
                val errorType = errorMapper.map(e)

                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = errorType)
            }
        }
    }

    fun onFeedNavigationRequested() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateToFeed)
        }
    }

    fun onPostNavigationRequested() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateToPost)
        }
    }

    fun onUpdateNavigationRequested() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateToUpdate)
        }
    }

    sealed class NavigationEvent {
        object SignOutSuccess : NavigationEvent()
        object NavigateToFeed : NavigationEvent()
        object NavigateToPost : NavigationEvent()
        object NavigateToUpdate : NavigationEvent()
    }

}