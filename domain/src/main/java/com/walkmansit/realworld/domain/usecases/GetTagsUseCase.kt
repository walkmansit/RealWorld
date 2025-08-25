package com.walkmansit.realworld.domain.usecases

import com.walkmansit.realworld.domain.repository.ArticleRepository
import com.walkmansit.realworld.domain.util.Either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetTagsUseCase(
    private val articleRepository: ArticleRepository,
) {
    suspend operator fun invoke(): Either<Boolean, List<String>> =
        withContext(Dispatchers.IO) {
            when (val articleResponse = articleRepository.getTags()) {
                is Either.Fail -> Either.fail(false)
                is Either.Success -> Either.success(articleResponse.value)
            }
        }
}
