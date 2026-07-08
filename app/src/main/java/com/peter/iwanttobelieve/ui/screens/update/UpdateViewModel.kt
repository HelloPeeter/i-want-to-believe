package com.peter.iwanttobelieve.ui.screens.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class UpdateViewModel @Inject constructor(
    private val repository: UserRepository,
    private val errorMapper: ErrorMapper
): ViewModel() {

    private val _uiState = MutableStateFlow(UpdateUiState())
    val uiState: StateFlow<UpdateUiState> = _uiState.asStateFlow()

    // Emite quando o usuário deve voltar para a tela de perfil (ex.: botão "Voltar").
    private val _navigationEvent = MutableSharedFlow<Unit>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        getCurrentUser()
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, isSuccess = false, errorMessage = null)
                val result = repository.getCurrentUser()

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

    // Atualiza nome e e-mail sem pedir a senha novamente a cada edição.
    fun updateProfile(email: String, name: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, isSuccess = false, errorMessage = null, successMessage = null)

                val result = repository.updateProfile(email, name)

                if (result.isSuccess) {
                    val updatedUser = _uiState.value.user?.copy(email = email, name = name)
                    _uiState.value = _uiState.value.copy(
                        user = updatedUser,
                        isLoading = false,
                        isSuccess = true,
                        successMessage = "Perfil atualizado com sucesso."
                    )
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

    // Trocar a senha é a única ação que ainda exige a senha atual.
    fun changePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, isSuccess = false, errorMessage = null, successMessage = null)

                val result = repository.changePassword(currentPassword, newPassword)

                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        successMessage = "Senha alterada com sucesso."
                    )
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

    fun onBackRequested() {
        viewModelScope.launch {
            _navigationEvent.emit(Unit)
        }
    }
}
