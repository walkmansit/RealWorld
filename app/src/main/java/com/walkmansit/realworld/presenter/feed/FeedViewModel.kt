package com.walkmansit.realworld.presenter.feed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.walkmansit.realworld.domain.model.Article
import com.walkmansit.realworld.domain.model.ArticleFilterType
import com.walkmansit.realworld.domain.model.ArticlesFilter
import com.walkmansit.realworld.domain.usecases.GetArticlesUseCase
import com.walkmansit.realworld.domain.usecases.LogoutUseCase
import com.walkmansit.realworld.presenter.feed.ArticlePagingSource.Companion.FEED_PAGE_SIZE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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
import kotlin.time.Duration.Companion.milliseconds

sealed interface FeedIntent : MVIIntent {
    data class ChangeFilter(
        val filter: ArticleFilterType,
    ) : FeedIntent

    data object RedirectNewArticle : FeedIntent

    data class RedirectArticle(
        val slug: String,
    ) : FeedIntent

    data object LogOut : FeedIntent
}

interface  FeedBaseState : MVIState {
    val filter: ArticleFilterType
    val articles: Flow<PagingData<Article>>
}


sealed interface FeedState : FeedBaseState {
    data object Loading : FeedState {
        override val filter: ArticleFilterType = ArticleFilterType.Feed
        override val articles: Flow<PagingData<Article>> = flowOf()
    }

    data class Error(
        val message: String,
        override val filter: ArticleFilterType,
        override val articles: Flow<PagingData<Article>>,
    ) : FeedState

    data class Content(
        override val filter: ArticleFilterType = ArticleFilterType.Feed,
        override val articles: Flow<PagingData<Article>> = flowOf()
    ) : FeedState

    data class LoadingOnSubmit(
        override val filter: ArticleFilterType = ArticleFilterType.Feed,
        override val articles: Flow<PagingData<Article>> = flowOf()
    ) : FeedState
}

sealed class FeedAction : MVIAction {

    data class RedirectArticle(
        val slug: String,
    ) : FeedAction()

    data object RedirectNewArticle : FeedAction()

    data object RedirectLogin : FeedAction()
}

private typealias Ctx = PipelineContext<FeedState, FeedIntent, FeedAction>


@HiltViewModel
class FeedViewModel
@Inject
constructor(
    private val getArticlesUseCase: GetArticlesUseCase,
    private val logoutUseCase: LogoutUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), Container<FeedState, FeedIntent, FeedAction> {
    //private val username: String = savedStateHandle[FeedDestinationsArgs.USERNAME_ARG]!!

    private val filterFlow = MutableStateFlow(ArticlesFilter(filterType = ArticleFilterType.Feed))

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val articles = filterFlow.debounce(300.milliseconds).flatMapLatest { filter ->
        Pager(
            PagingConfig(
                pageSize = FEED_PAGE_SIZE,
                enablePlaceholders = false,
            ),
        ) {
            ArticlePagingSource(
                getArticlesUseCase,
                filter,
                viewModelScope,
            )
        }.flow.cachedIn(viewModelScope)
    }

    override val store =
        store(
            initial = FeedState.Content(articles = articles),
            scope = viewModelScope
        ) {

            configure {
                name = "FeedStore"
                debuggable = true
                actionShareBehavior = ActionShareBehavior.Distribute()
                parallelIntents = true
            }

            recover { e: Exception ->
                updateState {
                    FeedState.Error(
                            message = e.message ?: "Unknown error",
                            filter = ArticleFilterType.Feed,
                            articles = flowOf()
                        )
                }
                null
            }

            reduce { intent ->
                when (intent) {
                    is FeedIntent.ChangeFilter -> changeFilter(intent.filter)
                    is FeedIntent.RedirectNewArticle -> action(FeedAction.RedirectNewArticle)
                    is FeedIntent.RedirectArticle -> action(FeedAction.RedirectArticle(intent.slug))
                    is FeedIntent.LogOut -> action(FeedAction.RedirectLogin)
                }
            }
        }

//    private val _uiState = MutableStateFlow(FeedUiState())
//    val uiState = _uiState.asStateFlow()
//
//    private val search: StateFlow<ArticlesFilter> =
//        _uiState
//            .asStateFlow()
//            .map { it.selectedFilter.toArticlesFilter(username) }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(),
//                initialValue = ArticlesFilter(_uiState.value.selectedFilter),
//            )

//    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
//    val articlesResult =
//        search.debounce(300.milliseconds).flatMapLatest { filter ->
//            Pager(
//                PagingConfig(
//                    pageSize = FEED_PAGE_SIZE,
//                    enablePlaceholders = false,
//                ),
//            ) {
//                ArticlePagingSource(
//                    getArticlesUseCase,
//                    filter,
//                    viewModelScope,
//                )
//            }.flow.cachedIn(viewModelScope)
//        }






    private suspend fun Ctx.changeFilter(filter: ArticleFilterType) {
        updateStateOrThrow<FeedState.Content, _> {
            FeedState.LoadingOnSubmit(filter, articles)
//            copy(
//                fi
//                filter = filter
//            )
//            intent()
        }
//        withState<FeedState.Content, _> {
//            action(FeedAction)
//        }
    }

    private fun Ctx.redirectComplete() {
        //_uiState.update { it.copy(navEvent = FeedAction.Undefined) }
    }

    private fun Ctx.logout() {
        viewModelScope.launch {
            val result = logoutUseCase.invoke()
            if (result) {
//                _uiState.update { it.copy(navEvent = FeedAction.RedirectLogin) }
            }
        }
    }
}
