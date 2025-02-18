package com.walkmansit.realworld.ui.feed

import androidx.lifecycle.ViewModel
import com.walkmansit.realworld.RwDestinations
import com.walkmansit.realworld.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

sealed interface FeedIntent {
    data object RedirectNewArticle : FeedIntent
}

data class FeedUiState(
    val isLoading: Boolean = false,
    val uiEvent: UiEvent = UiEvent.Undefined
)

@HiltViewModel
class FeedViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState = _uiState.asStateFlow()

    fun onIntent(intent: FeedIntent) {
        when (intent) {
            is FeedIntent.RedirectNewArticle -> redirectNewArticle()
        }
    }

    private fun redirectNewArticle() {
        _uiState.update { it.copy(uiEvent = UiEvent.NavigateEvent(RwDestinations.NEW_ARTICLE_ROUTE)) }
    }
}