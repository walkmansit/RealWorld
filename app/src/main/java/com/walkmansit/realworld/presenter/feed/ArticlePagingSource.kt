package com.walkmansit.realworld.presenter.feed

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
        const val FEED_PAGE_SIZE = 5
        const val STARTING_PAGE_INDEX = 0

    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: STARTING_PAGE_INDEX

        filter.limit = params.loadSize
        filter.offset = position

        return when(val resp = getArticlesUseCase(filter)){
            is Either.Success -> {
                val nextKey = if (resp.value.isEmpty()) {
                    null
                } else {
                    position + resp.value.size
                }

                LoadResult.Page(
                    resp.value,
                    prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                    nextKey = nextKey,
                )
            }
            is Either.Fail -> LoadResult.Error(Exception())
        }
    }


}