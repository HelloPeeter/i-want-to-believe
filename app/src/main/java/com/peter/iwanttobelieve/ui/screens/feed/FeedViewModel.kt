package com.peter.iwanttobelieve.ui.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.iwanttobelieve.ui.model.PostWithAuthor
import com.peter.iwanttobelieve.data.model.Post
import com.peter.iwanttobelieve.data.model.User
import com.peter.iwanttobelieve.data.repository.PostRepository
import com.peter.iwanttobelieve.data.repository.UserRepository
import com.peter.iwanttobelieve.util.ErrorMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val errorMapper: ErrorMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val userCache = mutableMapOf<String, User>()

    init {
        _uiState.value = _uiState.value.copy(currentUserId = userRepository.getCurrentUserIdOrNull())
    }

    fun observePosts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                isSuccess = false,
                errorMessage = null
            )

            postRepository.observePostsByTimestamp().collectLatest { result ->
                if (result.isSuccess) {
                    val posts = result.getOrNull().orEmpty()
                    val currentUserIds = posts.map { it.userId }.distinct()

                    val missingIds = currentUserIds.filter { !userCache.containsKey(it) }

                    if (missingIds.isNotEmpty()) {
                        val usersResult = userRepository.getUsersByIds(missingIds)

                        if (usersResult.isSuccess) {
                            val fetched = usersResult.getOrDefault(emptyMap())
                            userCache.putAll(fetched)

                            val keysToRemoveAfterFetch = userCache.keys.filter { it !in currentUserIds }
                            for (k in keysToRemoveAfterFetch) userCache.remove(k)

                            val updated = posts.map { post ->
                                PostWithAuthor(post = post, author = userCache[post.userId])
                            }
                            _uiState.value = _uiState.value.copy(
                                postsWithAuthor = updated,
                                isLoading = false,
                                isSuccess = true,
                                errorMessage = null
                            )
                        } else {
                            val errorType = errorMapper.map(usersResult.exceptionOrNull())
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                errorMessage = errorType
                            )
                        }
                    } else {
                        val keysToRemove = userCache.keys.filter { it !in currentUserIds }
                        for (k in keysToRemove) userCache.remove(k)

                        val finalList = posts.map { post ->
                            PostWithAuthor(post = post, author = userCache[post.userId])
                        }
                        _uiState.value = _uiState.value.copy(
                            postsWithAuthor = finalList,
                            isLoading = false,
                            isSuccess = true,
                            errorMessage = null
                        )
                    }
                } else {
                    val errorType = errorMapper.map(result.exceptionOrNull())
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = false,
                        errorMessage = errorType
                    )
                }
            }
        }
    }

    // Curte ou descurte um post. A lista de posts é atualizada automaticamente
    // pelo listener em tempo real do Firestore (observePostsByTimestamp), então
    // não é necessário atualizar o estado manualmente aqui.
    fun toggleLike(post: Post) {
        val currentUserId = _uiState.value.currentUserId ?: return
        val isCurrentlyLiked = post.likes.contains(currentUserId)

        viewModelScope.launch {
            val result = postRepository.toggleLike(post.id, isCurrentlyLiked)

            if (!result.isSuccess) {
                val errorType = errorMapper.map(result.exceptionOrNull())
                _uiState.value = _uiState.value.copy(errorMessage = errorType)
            }
        }
    }

    fun onPostNavigationRequested() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateToPost)
        }
    }

    fun onProfileNavigationRequested() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateToProfile)
        }
    }

    sealed class NavigationEvent {
        object NavigateToPost : NavigationEvent()
        object NavigateToProfile : NavigationEvent()
    }
}
