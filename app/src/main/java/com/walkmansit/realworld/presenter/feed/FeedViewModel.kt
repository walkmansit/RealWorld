package com.walkmansit.realworld.presenter.feed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.walkmansit.realworld.domain.model.ArticleFilterType
import com.walkmansit.realworld.domain.model.ArticlesFilter
import com.walkmansit.realworld.domain.usecases.GetArticlesUseCase
import com.walkmansit.realworld.domain.usecases.LogoutUseCase
import com.walkmansit.realworld.presenter.feed.ArticlePagingSource.Companion.FEED_PAGE_SIZE
import com.walkmansit.realworld.presenter.navigation.FeedDestinationsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

sealed interface FeedIntent {
    data class ChangeFilter(
        val filter: ArticleFilterType,
    ) : FeedIntent

    data object RedirectNewArticle : FeedIntent

    data class RedirectArticle(
        val slug: String,
    ) : FeedIntent

    data object RedirectComplete : FeedIntent

    data object LogOut : FeedIntent
}

data class FeedUiState(
    val selectedFilter: ArticleFilterType = ArticleFilterType.Feed,
    val isLoading: Boolean = false,
    val navEvent: FeedNavigationEvent = FeedNavigationEvent.Undefined,
)

sealed class FeedNavigationEvent {
    data object Undefined : FeedNavigationEvent()

    data class RedirectArticle(
        val slug: String,
    ) : FeedNavigationEvent()

    data object RedirectNewArticle : FeedNavigationEvent()

    data object RedirectLogin : FeedNavigationEvent()
}

@HiltViewModel
class FeedViewModel
@Inject
constructor(
    private val getArticlesUseCase: GetArticlesUseCase,
    private val logoutUseCase: LogoutUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val username: String = savedStateHandle[FeedDestinationsArgs.USERNAME_ARG]!!

//    private lateinit var _user: User
//
//    init {
//        runBlocking {
//            val localUserResp = checkAuthUseCase()
//            if (localUserResp is Either.Success)
//                _user = localUserResp.value
//        }
//    }

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState = _uiState.asStateFlow()

    private val search: StateFlow<ArticlesFilter> =
        _uiState
            .asStateFlow()
            .map { it.selectedFilter.toArticlesFilter(username) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = ArticlesFilter(_uiState.value.selectedFilter),
            )

//    private val search : StateFlow<ArticlesFilter> = _filter.asStateFlow()
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(),
//            initialValue = ArticlesFilter(),
//        )

    fun onIntent(intent: FeedIntent) {
        when (intent) {
            is FeedIntent.ChangeFilter -> changeFilter(intent.filter)
            is FeedIntent.RedirectNewArticle -> redirectNewArticle()
            is FeedIntent.RedirectArticle -> redirectArticle(intent.slug)
            is FeedIntent.RedirectComplete -> redirectComplete()
            is FeedIntent.LogOut -> logout()
        }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val articlesResult =
        search.debounce(300.milliseconds).flatMapLatest { filter ->
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

    private fun redirectNewArticle() {
        _uiState.update { it.copy(navEvent = FeedNavigationEvent.RedirectNewArticle) }
    }

    private fun redirectArticle(slug: String) {
        _uiState.update { it.copy(navEvent = FeedNavigationEvent.RedirectArticle(slug)) }
    }

    private fun changeFilter(filter: ArticleFilterType) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }

    private fun redirectComplete() {
        _uiState.update { it.copy(navEvent = FeedNavigationEvent.Undefined) }
    }

    private fun logout() {
        viewModelScope.launch {
            val result = logoutUseCase.invoke()
            if (result) {
                _uiState.update { it.copy(navEvent = FeedNavigationEvent.RedirectLogin) }
            }
        }
    }
}
