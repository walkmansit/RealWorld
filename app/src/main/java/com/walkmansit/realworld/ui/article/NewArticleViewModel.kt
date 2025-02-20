package com.walkmansit.realworld.ui.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walkmansit.realworld.RwDestinations
import com.walkmansit.realworld.UiEvent
import com.walkmansit.realworld.common.TextFieldState
import com.walkmansit.realworld.domain.repository.ArticleRepository
import com.walkmansit.realworld.domain.use_case.NewArticleUseCase
import com.walkmansit.realworld.domain.util.Either
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface NewArticleIntent {
    data class UpdateTitleIntent(val title: String) : NewArticleIntent
    data class UpdateDescriptionIntent(val description: String) : NewArticleIntent
    data class UpdateBodyIntent(val body: String) : NewArticleIntent
    data class UpdateSearchQueryIntent(val query: String) : NewArticleIntent
    data object SubmitTag : NewArticleIntent
    data class DeleteTag(val tag: Tag) : NewArticleIntent
    data class AddTag(val tag: Tag) : NewArticleIntent
    data object Submit : NewArticleIntent
}

data class NewArticleUiState(
    val title: TextFieldState = TextFieldState(),
    val description: TextFieldState = TextFieldState(),
    val body: TextFieldState = TextFieldState(),
    val searchQuery: TextFieldState = TextFieldState(),
    val searchResult: List<Tag> = listOf(),
    val selectedTags: List<Tag> = listOf(),
    val isLoading: Boolean = false,
    val uiEvent: UiEvent = UiEvent.Undefined
)

data class Tag(
    val id: Int,
    val value: String,
)

@HiltViewModel
class NewArticleViewModel @Inject constructor(
    private val newArticleUseCase: NewArticleUseCase,
    private val repository: ArticleRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewArticleUiState())
    val uiState = _uiState.asStateFlow()

    private val _selectedTags : MutableSet<Tag> = mutableSetOf()

    private var _allTagsShadow : MutableSet<Tag> = mutableSetOf()
    private val _allTags : MutableStateFlow<Set<Tag>> = MutableStateFlow(setOf())
    private val _searchQuery : MutableStateFlow<String> = MutableStateFlow("")
    private var tagIdx = 0

    init {
        _uiState.update {
            it.copy(selectedTags  = _selectedTags.toList())
        }
        fetchTags(_selectedTags.size)
    }

    fun onIntent(intent: NewArticleIntent) {
        when (intent) {
            is NewArticleIntent.UpdateTitleIntent -> updateTitle(intent.title)
            is NewArticleIntent.UpdateDescriptionIntent -> updateDescription(intent.description)
            is NewArticleIntent.UpdateBodyIntent -> updateBody(intent.body)
            is NewArticleIntent.UpdateSearchQueryIntent -> updateSearchQuery(intent.query)
            is NewArticleIntent.SubmitTag -> submitTag()
            is NewArticleIntent.DeleteTag -> deleteTag(intent.tag)
            is NewArticleIntent.AddTag -> addTag(intent.tag)
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

    private fun updateSearchQuery(query: String){
        //activate combine and filter results
        _searchQuery.value = query
        //update ui state
        _uiState.update { it.copy(searchQuery = TextFieldState(text = query)) }
    }

    private fun addTag(tag: Tag){
        viewModelScope.launch {
            if (!_selectedTags.contains(tag)){
                _selectedTags.add(tag)
                _uiState.update { it.copy(selectedTags = _selectedTags.toList()) }

                //remove tag from all tags and run side effect
                _allTagsShadow.remove(tag)
                _allTags.value = _allTagsShadow.toSet()
            }
        }
    }

    private fun submitTag(){
        val tagValue = _uiState.value.searchQuery.text
        if (tagValue.isNotEmpty()
            && _selectedTags.none { it.value.lowercase() == tagValue }
            && _allTags.value.none { it.value.lowercase() == tagValue }
        ){
            tagIdx += 1
            _selectedTags.add(Tag(tagIdx, tagValue))
            _uiState.update {
                it.copy(selectedTags  = _selectedTags.toList(), searchQuery = TextFieldState())
            }
        }
    }

    private fun deleteTag(tag: Tag){
        viewModelScope.launch {
            if (_selectedTags.contains(tag)) {
                _selectedTags.remove(tag)
                _uiState.update {
                    it.copy(selectedTags = _selectedTags.toList())
                }

                _allTagsShadow.add(tag)
                _allTags.value = _allTagsShadow.toSet()
            }
        }
    }

    private fun fetchTags(idxOffset: Int){
        viewModelScope.launch {
            val tagsRes = repository.getTags()
            if(tagsRes is Either.Success){
                _allTagsShadow = tagsRes.value
                    .mapIndexed { index, s -> Tag(index + idxOffset, s).also { tagIdx = index + idxOffset  } }
                    .toMutableSet()
                _allTags.value = _allTagsShadow.toSet()
            }
        }
    }

    val searchResult: StateFlow<List<Tag>> = combine(_searchQuery ,_allTags) { query, tags ->
            tags.toList().filter { it.value.contains(query) }
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = emptyList(),
            started = SharingStarted.WhileSubscribed(5_000)
        )

    private fun submitNewArticle() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val newArticleResp = newArticleUseCase(
                uiState.value.title.text,
                uiState.value.description.text,
                uiState.value.body.text,
                _selectedTags.toList().map { it.value }
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