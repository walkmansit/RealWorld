package com.walkmansit.realworld.ui.feed

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.walkmansit.realworld.domain.model.Article
import com.walkmansit.realworld.domain.model.ArticlesFilter
import com.walkmansit.realworld.domain.use_case.GetArticlesUseCase
import com.walkmansit.realworld.domain.util.Either

class ArticlePagingSource(
    private val getArticlesUseCase: GetArticlesUseCase,
    private val filter: ArticlesFilter,
) : PagingSource<Int, Article>() {

    companion object {
        const val PAGE_SIZE = 20
        private const val INITIAL_LOAD_SIZE = 0
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: INITIAL_LOAD_SIZE
        val offset = if (params.key != null) ((position - 1) * PAGE_SIZE) else INITIAL_LOAD_SIZE

        return when(val resp = getArticlesUseCase(filter)){
            is Either.Success -> {
                val nextKey = if (resp.value.isEmpty()) {
                    null
                } else {
                    position + (params.loadSize / PAGE_SIZE)
                }

                LoadResult.Page(
                    resp.value,
                    prevKey = null,
                    nextKey = nextKey,
                )
            }
            is Either.Fail -> LoadResult.Error(Exception())
        }
    }

}