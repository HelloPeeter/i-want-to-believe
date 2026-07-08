package com.peter.iwanttobelieve.ui.screens.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.iwanttobelieve.data.repository.PostRepository
import com.peter.iwanttobelieve.data.repository.UserRepository
import com.peter.iwanttobelieve.util.ErrorMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val errorMapper: ErrorMapper
): ViewModel() {

    private val _uiState = MutableStateFlow(PostUiState())
    val uiState: StateFlow<PostUiState> = _uiState.asStateFlow()

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
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = errorType)
                }
            } catch (e: Exception) {
                val errorType = errorMapper.map(e)
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = errorType)
            }
        }
    }

    fun createPost(image: ByteArray, description: String) {
        viewModelScope.launch {
            try {

                _uiState.value = _uiState.value.copy(isLoading = true, isSuccess = false, errorMessage = null)

                if (description.isBlank()) {
                    val errorType = errorMapper.map(IllegalArgumentException("A descrição não pode estar vazia."))
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = errorType)
                    return@launch
                }

                val result = postRepository.createPostWithImage(image, description)

                if(result.isSuccess) {
                    _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)

                    _navigationEvent.emit(NavigationEvent.NavigateToFeed)
                }
                else {
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

    fun onProfileNavigationRequested() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateToProfile)
        }
    }

    sealed class NavigationEvent {
        object NavigateToFeed : NavigationEvent()
        object NavigateToProfile : NavigationEvent()
    }

}