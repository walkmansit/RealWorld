package com.walkmansit.realworld.ui.article

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walkmansit.realworld.ArticleDestinationsArgs.SLUG_ARG
import com.walkmansit.realworld.common.TextFieldState
import com.walkmansit.realworld.domain.model.Article
import com.walkmansit.realworld.domain.model.Tag
import com.walkmansit.realworld.domain.use_case.GetArticleUseCase
import com.walkmansit.realworld.domain.use_case.GetTagsUseCase
import com.walkmansit.realworld.domain.util.Either
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ArticleUiState {
    data class ArticleUiData(
        val title: TextFieldState = TextFieldState(),
        val description: TextFieldState = TextFieldState(),
        val body: TextFieldState = TextFieldState(),
        val selectedTags: List<Tag> = listOf(),
    ) : ArticleUiState
    data object IsLoading : ArticleUiState
    data class HasError(val errorMsg: String) : ArticleUiState
}

//data class ArticleUiData(
//    val isLoading: Boolean = false,
//    val errorMessage: String? = null,
//    val uiEvent: UiEvent = UiEvent.Undefined
//)

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val getArticleUseCase: GetArticleUseCase,
    private val getTagsUseCase: GetTagsUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _tagsFlow = MutableStateFlow<List<Tag>?>(null)
    private val _articleFlow = MutableStateFlow<Article?>(null)

    private val _slug: String = savedStateHandle[SLUG_ARG]!!

    private val _uiState = MutableStateFlow<ArticleUiState>(ArticleUiState.IsLoading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchData()
    }



//    private val _canEdit: Boolean = savedStateHandle[CAN_EDIT_ARG]!!


    private fun fetchData(){
        viewModelScope.launch {
            _uiState.update { ArticleUiState.IsLoading }

            fetchArticle()
            fetchTags()

            combine(_tagsFlow.filterNotNull(), _articleFlow.filterNotNull()){
                tag, article -> tag to article
            }.collectLatest { pair ->
                _uiState.update {
                    ArticleUiState.ArticleUiData(
                        title = TextFieldState(pair.second.title),
                        description = TextFieldState(pair.second.description),
                        body = TextFieldState(pair.second.body!!),
                        selectedTags = pair.first,
                    )
                }
            }
        }
    }

    private suspend fun fetchArticle(){
        when (val artRes = getArticleUseCase(_slug)) {
            is Either.Fail -> {
                _uiState.update { ArticleUiState.HasError("Failed to load data") }
            }
            is Either.Success -> {
                _articleFlow.value = artRes.value
            }
        }
    }

    private suspend fun fetchTags(){
        when (val tagRes = getTagsUseCase()) {
            is Either.Fail -> {
                _uiState.update { ArticleUiState.HasError("Failed to load tags") }
            }
            is Either.Success -> {
                _tagsFlow.value = tagRes.value.mapIndexed { index, s -> Tag(index, s) }
            }
        }

    }


}