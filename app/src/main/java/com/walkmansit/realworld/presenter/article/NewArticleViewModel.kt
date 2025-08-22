package com.walkmansit.realworld.presenter.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walkmansit.realworld.presenter.components.TextFieldState
import com.walkmansit.realworld.domain.model.Article
import com.walkmansit.realworld.domain.model.NewArticleFailed
import com.walkmansit.realworld.domain.use_case.GetTagsUseCase
import com.walkmansit.realworld.domain.use_case.NewArticleUseCase
import com.walkmansit.realworld.domain.util.Either
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
import javax.inject.Inject

sealed interface NewArticleIntent : MVIIntent {
    data class UpdateTitle(val title: String) : NewArticleIntent
    data class UpdateDescription(val description: String) : NewArticleIntent
    data class UpdateBody(val body: String) : NewArticleIntent
    data object Submit : NewArticleIntent
    data class SubmitComplete(val result: Either<NewArticleFailed, Article>) : NewArticleIntent
}

sealed interface NewArticleState : MVIState {
    data object Loading : NewArticleState
    data class Error(val message: String) : NewArticleState
    data class Content(val content: NewArticleFields = NewArticleFields()) : NewArticleState
    data class LoadingOnSubmit(val content: NewArticleFields ) : NewArticleState
    //data class LoadingOnSubmit(val fields: RegistrationFields = RegistrationFields()) : NewArticleState
}

sealed interface NewArticleAction : MVIAction {
    data object SubmitComplete : NewArticleAction
}

data class NewArticleFields(
    val title: TextFieldState = TextFieldState(),
    val description: TextFieldState = TextFieldState(),
    val body: TextFieldState = TextFieldState(),
)

private typealias Ctx = PipelineContext<NewArticleState, NewArticleIntent, NewArticleAction>

@HiltViewModel
class NewArticleViewModel @Inject constructor(
    private val newArticleUseCase: NewArticleUseCase,
    private val getTagsUseCase: GetTagsUseCase,
) : ViewModel(), Container<NewArticleState, NewArticleIntent, NewArticleAction> {

    override val store = store(initial = NewArticleState.Content(), viewModelScope){
        configure {
            name = "NewArticleStore"
            debuggable = true
            actionShareBehavior = ActionShareBehavior.Distribute()
            parallelIntents = true
        }

        recover { e: Exception ->
            updateState {
                NewArticleState.Error(e.message ?: "Unknown error")
            }
            null
        }

        reduce { intent ->
            when (intent) {
                is NewArticleIntent.UpdateTitle -> updateTitle(intent.title)
                is NewArticleIntent.UpdateDescription -> updateDescription(intent.description)
                is NewArticleIntent.UpdateBody -> updateBody(intent.body)
                is NewArticleIntent.Submit -> submitNewArticle()
                is NewArticleIntent.SubmitComplete -> {}
            }
        }

    }


    private suspend fun Ctx.updateDescription(newValue: String) {
        updateStateOrThrow<NewArticleState.Content, _> {
            with(content){
                copy(content = copy(
                    description = description.copy(text = newValue)
                ))
            }
        }
    }

    private suspend fun Ctx.updateTitle(newValue: String) {
        updateStateOrThrow<NewArticleState.Content, _> {
            with(content){
                copy(content = copy(
                    title = title.copy(text = newValue)
                ))
            }
        }
    }

    private suspend fun Ctx.updateBody(newValue: String) {
        updateStateOrThrow<NewArticleState.Content, _> {
            with(content){
                copy(content = copy(
                    body = body.copy(text = newValue)
                ))
            }
        }
    }

    private suspend fun Ctx.submitComplete(result: Either<NewArticleFailed, Article>){
        updateStateOrThrow<NewArticleState.LoadingOnSubmit, _> {
            when(result){
                is Either.Fail -> {
                    with(content){
                        val newFields = copy(
                            description = description.copy(error = result.value.descriptionError),
                            title = title.copy(error = result.value.titleError),
                            body = body.copy(error = result.value.bodyError),
                        )
                        NewArticleState.Content(newFields)
                    }
                }
                is Either.Success -> {
                    action(NewArticleAction.SubmitComplete)
                    NewArticleState.Content()
                }
            }
        }

    }

    private suspend fun Ctx.submitNewArticle() {
        updateStateOrThrow<NewArticleState.Content, _> {
            with(content){
                viewModelScope.launch {
                    val newArticleResponse = newArticleUseCase(
                        title = title.text,
                        description = description.text,
                        body = body.text,
                        tags = listOf(),
                    )

                    intent(NewArticleIntent.SubmitComplete(newArticleResponse))
                }
            }
            NewArticleState.LoadingOnSubmit(content)
        }
    }
}


