package com.walkmansit.realworld

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walkmansit.realworld.domain.use_case.CheckAuthUseCase
import com.walkmansit.realworld.domain.util.Either
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SplashUiState(
    val isLoading: Boolean = false,
    val navEvent: SplashNavigationEvent = SplashNavigationEvent.Undefined
)

sealed class SplashNavigationEvent {
    data object Undefined : SplashNavigationEvent()
    data class RedirectFeed(val username: String) : SplashNavigationEvent()
    data object RedirectLogin : SplashNavigationEvent()
    data object RedirectRegistration : SplashNavigationEvent()
}

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val checkAuthUseCase: CheckAuthUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            when (val authResult = checkAuthUseCase()) {
                is Either.Fail -> {
                    when (authResult.value) {
                        is Either.Fail -> {
                            _uiState.update { it.copy(navEvent = SplashNavigationEvent.RedirectRegistration) }
                        }

                        is Either.Success -> {
                            _uiState.update { it.copy(navEvent = SplashNavigationEvent.RedirectLogin) }
                        }
                    }
                }

                is Either.Success -> {
                    _uiState.update { it.copy(navEvent = SplashNavigationEvent.RedirectFeed(authResult.value.username)) }
                }
            }
        }
    }

    fun consumeNavEvent(){
        _uiState.update { it.copy(navEvent = SplashNavigationEvent.Undefined) }
    }
}