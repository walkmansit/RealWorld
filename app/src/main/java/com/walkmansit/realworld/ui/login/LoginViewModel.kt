package com.walkmansit.realworld.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walkmansit.realworld.RwDestinations
import com.walkmansit.realworld.UiEvent
import com.walkmansit.realworld.common.TextFieldState
import com.walkmansit.realworld.domain.use_case.LoginUseCase
import com.walkmansit.realworld.domain.util.Either
import com.walkmansit.realworld.ui.registration.RegistrationIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: TextFieldState = TextFieldState(),
    val password: TextFieldState = TextFieldState(),
    val isLoading: Boolean = false,
    val uiEvent: UiEvent = UiEvent.Undefined,
)

sealed interface LoginIntent {
    data class UpdateEmail(val email: String) : LoginIntent
    data class UpdatePassword(val password: String) : LoginIntent
    data object Submit : LoginIntent
    data object RedirectRegistration : LoginIntent
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onIntent(intent: LoginIntent){
        when(intent){
            is LoginIntent.UpdateEmail -> updateEmail(intent.email)
            is LoginIntent.UpdatePassword -> updatePassword(intent.password)
            is LoginIntent.Submit -> loginUser()
            is LoginIntent.RedirectRegistration -> redirectRegistration()
        }
    }

    private fun updateEmail(newValue: String) {
        _uiState.update { it.copy(email = TextFieldState(newValue)) }
    }

    private fun updatePassword(newValue: String) {
        _uiState.update { it.copy(password = TextFieldState(newValue)) }
    }

    private fun loginUser(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val loginResult = with(_uiState.value) {
                loginUseCase(
                    email = email.text,
                    password = password.text
                )
            }

            _uiState.update { it.copy(isLoading = false) }

            when(loginResult){
                is Either.Fail -> {
                    _uiState.update {
                        it.copy(
                            email = it.email.copy(error = loginResult.value.emailError),
                            password = it.password.copy(error = loginResult.value.passwordError),
                        )
                    }
                }
                is Either.Success -> {
                    //TODO navigate to root menu
                }
            }
        }
    }

    private fun redirectRegistration(){
        _uiState.update { it.copy(uiEvent = UiEvent.NavigateEvent(RwDestinations.REGISTRATION_ROUTE)) }
    }
}