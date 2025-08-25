package com.walkmansit.realworld.presenter.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walkmansit.realworld.domain.usecases.GetTagsUseCase
import com.walkmansit.realworld.domain.util.Either
import com.walkmansit.realworld.presenter.components.TextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pro.respawn.flowmvi.api.ActionShareBehavior
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.MVIAction
import pro.respawn.flowmvi.api.MVIIntent
import pro.respawn.flowmvi.api.MVIState
import pro.respawn.flowmvi.api.PipelineContext
import pro.respawn.flowmvi.dsl.store
import pro.respawn.flowmvi.dsl.updateStateOrThrow
import pro.respawn.flowmvi.plugins.recover
import pro.respawn.flowmvi.plugins.reduce
import pro.respawn.flowmvi.plugins.whileSubscribed
import javax.inject.Inject

sealed interface TagsIntent : MVIIntent {
    data class UpdateSearchQuery(
        val query: String,
    ) : TagsIntent

    data class SubmitNewTag(
        val tag: String,
    ) : TagsIntent

    data class TagsLoaded(
        val result: Either<Boolean, List<String>>,
    ) : TagsIntent

    data class DeleteTag(
        val tag: String,
    ) : TagsIntent

    data class AddTag(
        val tag: String,
    ) : TagsIntent
}

sealed interface TagState : MVIState {
    data object Loading : TagState

    data class Error(
        val message: String,
    ) : TagState

    data class Content(
        val content: TagFields = TagFields(),
    ) : TagState
}

sealed interface TagAction : MVIAction

data class TagFields(
    val searchQuery: TextFieldState = TextFieldState(),
    val searchResult: List<String> = listOf(),
    val selectedTags: List<String> = listOf(),
    val allTags: List<String> = listOf(),
)

private typealias TagsCtx = PipelineContext<TagState, TagsIntent, TagAction>

@HiltViewModel
class TagsViewModel
@Inject
constructor(
    private val getTagsUseCase: GetTagsUseCase,
) : ViewModel(),
    Container<TagState, TagsIntent, TagAction> {
    override val store =
        store(initial = TagState.Loading, scope = viewModelScope) {
            configure {
                name = "TagStore"
                debuggable = true
                actionShareBehavior = ActionShareBehavior.Distribute()
                parallelIntents = true
            }

            recover { e: Exception ->
                updateState {
                    TagState.Error(e.message ?: "Unknown error")
                }
                null
            }

            whileSubscribed {
                loadTags()
            }

            reduce { intent ->
                when (intent) {
                    is TagsIntent.UpdateSearchQuery -> onUpdateSearchQuery(intent.query)
                    is TagsIntent.SubmitNewTag -> submitNewTag(intent.tag)
                    is TagsIntent.TagsLoaded -> onTagsLoaded(intent.result)
                    is TagsIntent.DeleteTag -> deleteTag(intent.tag)
                    is TagsIntent.AddTag -> addTag(intent.tag)
                }
            }
        }

    private val selectedTags = mutableSetOf<String>()
    private val allTags = mutableSetOf<String>()

    private suspend fun TagsCtx.addTag(tag: String) {
        if (allTags.contains(tag)) {
            updateStateOrThrow<TagState.Content, _> {
                allTags.remove(tag)
                selectedTags.add(tag)
                with(content) {
                    copy(
                        content =
                            copy(
                                selectedTags = selectedTags.toList(),
                                allTags = allTags.toList(),
                                searchResult = searchResult.minus(tag),
                            ),
                    )
                }
            }
        }
    }

    private suspend fun TagsCtx.deleteTag(tag: String) {
        if (selectedTags.contains(tag)) {
            updateStateOrThrow<TagState.Content, _> {
                selectedTags.remove(tag)
                allTags.add(tag)
                with(content) {
                    copy(
                        content =
                            copy(
                                selectedTags = selectedTags.toList(),
                                allTags = allTags.toList(),
                            ),
                    )
                }
            }
        }
    }

    private suspend fun TagsCtx.submitNewTag(tag: String) {
        if (tag.isNotEmpty() &&
            selectedTags.none { it.lowercase() == tag } &&
            allTags.none { it.lowercase() == tag }
        ) {
            updateStateOrThrow<TagState.Content, _> {
                selectedTags.add(tag)
                with(content) {
                    copy(
                        content =
                            copy(
                                selectedTags = selectedTags.toList(),
                            ),
                    )
                }
            }
        }
    }

    private fun filterTags(
        query: String,
        tags: List<String>,
    ): List<String> = tags.filter { tag -> tag.contains(query) }

    private suspend fun TagsCtx.onUpdateSearchQuery(query: String) {
        updateStateOrThrow<TagState.Content, _> {
            with(content) {
                copy(
                    content =
                        copy(
                            searchQuery = content.searchQuery.copy(text = query),
                            searchResult = filterTags(query, content.allTags),
                        ),
                )
            }
        }
    }

    private suspend fun TagsCtx.onTagsLoaded(tagsResult: Either<Boolean, List<String>>) {
        updateStateOrThrow<TagState.Loading, _> {
            when (tagsResult) {
                is Either.Fail -> TagState.Error("Failed to load tags")
                is Either.Success -> {
                    allTags.addAll(tagsResult.value)
                    TagState.Content(
                        content =
                            TagFields(
                                allTags = tagsResult.value,
                            ),
                    )
                }
            }
        }
    }

    private fun TagsCtx.loadTags() {
        viewModelScope.launch {
            intent(TagsIntent.TagsLoaded(getTagsUseCase()))
        }
    }
}
