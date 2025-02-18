package com.walkmansit.realworld.ui.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walkmansit.realworld.RwDestinations
import com.walkmansit.realworld.UiEvent
import com.walkmansit.realworld.common.TextFieldState
import com.walkmansit.realworld.domain.use_case.RegistrationUseCase
import com.walkmansit.realworld.domain.util.Either
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface RegistrationIntent {
    data class UpdateUserName(val username: String) : RegistrationIntent
    data class UpdateEmail(val email: String) : RegistrationIntent
    data class UpdatePassword(val password: String) : RegistrationIntent
    data object Submit : RegistrationIntent
    data object RedirectLogin : RegistrationIntent
}

data class RegistrationUiState(
    val username: TextFieldState = TextFieldState(),
    val email: TextFieldState = TextFieldState(),
    val password: TextFieldState = TextFieldState(),
    val isLoading: Boolean = false,
    val uiEvent: UiEvent = UiEvent.Undefined
)

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registrationUseCase: RegistrationUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState = _uiState.asStateFlow()

    fun onIntent(intent: RegistrationIntent) {
        when (intent) {
            is RegistrationIntent.UpdateUserName -> updateUserName(intent.username)
            is RegistrationIntent.UpdateEmail -> updateMail(intent.email)
            is RegistrationIntent.UpdatePassword -> updatePassword(intent.password)
            is RegistrationIntent.Submit -> registerUser()
            is RegistrationIntent.RedirectLogin -> redirectLogin()
        }
    }

    private fun updateUserName(newValue: String) {
        _uiState.update {
            it.copy(username = TextFieldState(newValue))
        }
    }

    private fun updateMail(newValue: String) {
        _uiState.update {
            it.copy(email = TextFieldState(newValue))
        }
    }

    private fun updatePassword(newValue: String) {
        _uiState.update {
            it.copy(password = TextFieldState(newValue))
        }
    }

    private fun registerUser() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val regUserResponse = registrationUseCase(
                _uiState.value.username.text,
                _uiState.value.email.text,
                _uiState.value.password.text,
            )

            _uiState.update { it.copy(isLoading = false) }

            when (regUserResponse) {
                is Either.Success -> {
                    _uiState.update { it.copy(uiEvent = UiEvent.NavigateEvent(RwDestinations.FEED_ROUTE)) }
                }

                is Either.Fail -> {
                    _uiState.update {
                        it.copy(
                            username = it.username.copy(error = regUserResponse.value.usernameError),
                            email = it.email.copy(error = regUserResponse.value.emailError),
                            password = it.password.copy(error = regUserResponse.value.passwordError),
                        )
                    }
                }
            }
        }
    }

    private fun redirectLogin() {
        _uiState.update { it.copy(uiEvent = UiEvent.NavigateEvent(RwDestinations.LOGIN_ROUTE)) }
    }
}