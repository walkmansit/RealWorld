package com.walkmansit.realworld.domain.usecases

import com.walkmansit.realworld.domain.model.Article
import com.walkmansit.realworld.domain.model.ArticlesFilter
import com.walkmansit.realworld.domain.repository.ArticleRepository
import com.walkmansit.realworld.domain.util.DispatcherProvider
import com.walkmansit.realworld.domain.util.Either
import kotlinx.coroutines.withContext

class GetArticlesUseCase(
    private val articleRepository: ArticleRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    suspend operator fun invoke(filter: ArticlesFilter): Either<Boolean, List<Article>> =
        withContext(dispatcherProvider.io) {
            when (val articlesResponse = articleRepository.getArticles(filter)) {
                is Either.Fail -> Either.fail(false)
                is Either.Success -> articlesResponse
            }
        }
}
