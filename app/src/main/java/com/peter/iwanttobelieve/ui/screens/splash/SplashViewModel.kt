package com.peter.iwanttobelieve.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.iwanttobelieve.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun checkUserAuthentication() {
        viewModelScope.launch {
            val result = repository.isUserLoggedIn()

            if (result.isSuccess) {
                val isLoggedIn = result.getOrNull() ?: false

                if(isLoggedIn) {
                    _navigationEvent.emit(NavigationEvent.NavigateToFeed)
                } else {
                    _navigationEvent.emit(NavigationEvent.NavigateToSignIn)
                }
            } else {
                _navigationEvent.emit(NavigationEvent.NavigateToSignIn)
            }
        }
    }

    sealed class NavigationEvent {
        object NavigateToFeed : NavigationEvent()
        object NavigateToSignIn : NavigationEvent()
    }
}