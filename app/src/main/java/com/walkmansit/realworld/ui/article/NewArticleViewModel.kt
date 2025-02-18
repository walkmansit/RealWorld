package com.walkmansit.realworld.ui.article

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walkmansit.realworld.RwDestinations
import com.walkmansit.realworld.UiEvent
import com.walkmansit.realworld.common.TextFieldState
import com.walkmansit.realworld.domain.use_case.NewArticleUseCase
import com.walkmansit.realworld.domain.util.Either
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface NewArticleIntent {
    data class UpdateTitleIntent(val title: String) : NewArticleIntent
    data class UpdateDescriptionIntent(val description: String) : NewArticleIntent
    data class UpdateBodyIntent(val body: String) : NewArticleIntent
    data object Submit : NewArticleIntent
}

data class NewArticleUiState(
    val title: TextFieldState = TextFieldState(),
    val description: TextFieldState = TextFieldState(),
    val body: TextFieldState = TextFieldState(),
    val tags: List<String> = listOf(),
    val isLoading: Boolean = false,
    val uiEvent: UiEvent = UiEvent.Undefined
)


@HiltViewModel
class NewArticleViewModel @Inject constructor(
    private val newArticleUseCase: NewArticleUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewArticleUiState())
    val uiState = _uiState.asStateFlow()

    fun onIntent(intent: NewArticleIntent) {
        when (intent) {
            is NewArticleIntent.UpdateTitleIntent -> updateTitle(intent.title)
            is NewArticleIntent.UpdateDescriptionIntent -> updateDescription(intent.description)
            is NewArticleIntent.UpdateBodyIntent -> updateBody(intent.body)
            is NewArticleIntent.Submit -> submitNewArticle()
        }
    }

    private fun updateDescription(newValue: String) {
        _uiState.update {
            it.copy(description = TextFieldState(newValue))
        }
    }

    private fun updateTitle(newValue: String) {
        _uiState.update {
            it.copy(title = TextFieldState(newValue))
        }
    }

    private fun updateBody(newValue: String) {
        _uiState.update {
            it.copy(body = TextFieldState(newValue))
        }
    }

    private fun submitNewArticle() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val newArticleResp = newArticleUseCase(
                uiState.value.title.text,
                uiState.value.description.text,
                uiState.value.body.text,
            )

            _uiState.update { it.copy(isLoading = false) }


            when (newArticleResp) {
                is Either.Success -> {
                    _uiState.update { it.copy(uiEvent = UiEvent.NavigateEvent(RwDestinations.FEED_ROUTE)) }
                }

                is Either.Fail -> {
                    _uiState.update {
                        it.copy(
                            title = it.title.copy(error = newArticleResp.value.titleError),
                            description = it.description.copy(error = newArticleResp.value.descriptionError),
                            body = it.body.copy(error = newArticleResp.value.bodyError),
                        )
                    }
                }
            }

        }
    }
}