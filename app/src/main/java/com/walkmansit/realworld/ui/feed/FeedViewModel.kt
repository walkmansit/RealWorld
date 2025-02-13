package com.walkmansit.realworld.ui.feed

import com.walkmansit.realworld.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface FeedIntent

data class FeedUiState(
    val isLoading : Boolean = false,
    val uiEvent: UiEvent = UiEvent.Undefined
)

@HiltViewModel
class FeedViewModel {
    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState = _uiState.asStateFlow()
}