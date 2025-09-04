package com.walkmansit.realworld.domain.usecases

import com.walkmansit.realworld.domain.model.Article
import com.walkmansit.realworld.domain.repository.ArticleRepository
import com.walkmansit.realworld.domain.util.DispatcherProvider
import com.walkmansit.realworld.domain.util.Either
import kotlinx.coroutines.withContext

class GetArticleUseCase(
    private val articleRepository: ArticleRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    suspend operator fun invoke(slug: String): Either<Boolean, Article> =
        withContext(dispatcherProvider.io) {
            when (val articleResponse = articleRepository.getArticle(slug)) {
                is Either.Fail -> Either.fail(false)
                is Either.Success -> articleResponse
            }
        }
}
