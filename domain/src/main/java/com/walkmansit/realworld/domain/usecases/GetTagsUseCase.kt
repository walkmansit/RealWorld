package com.walkmansit.realworld.domain.usecases

import com.walkmansit.realworld.domain.repository.ArticleRepository
import com.walkmansit.realworld.domain.util.DispatcherProvider
import com.walkmansit.realworld.domain.util.Either
import kotlinx.coroutines.withContext

class GetTagsUseCase(
    private val articleRepository: ArticleRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    suspend operator fun invoke(): Either<Boolean, List<String>> =
        withContext(dispatcherProvider.io) {
            when (val articleResponse = articleRepository.getTags()) {
                is Either.Fail -> Either.fail(false)
                is Either.Success -> Either.success(articleResponse.value)
            }
        }
}
