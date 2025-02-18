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
    val uiEvent: UiEvent = UiEvent.Undefined
)

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val checkAuthUseCase: CheckAuthUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            when (val authResult = checkAuthUseCase()) {
                is Either.Fail -> {
                    when (authResult.value) {
                        is Either.Fail -> {
                            _uiState.update { it.copy(uiEvent = UiEvent.NavigateEvent(RwDestinations.REGISTRATION_ROUTE)) }
                        }

                        is Either.Success -> {
                            _uiState.update { it.copy(uiEvent = UiEvent.NavigateEvent(RwDestinations.LOGIN_ROUTE)) }
                        }
                    }
                }

                is Either.Success -> {
                    _uiState.update { it.copy(uiEvent = UiEvent.NavigateEvent(RwDestinations.FEED_ROUTE)) }
                }
            }
        }
    }

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState = _uiState.asStateFlow()
}