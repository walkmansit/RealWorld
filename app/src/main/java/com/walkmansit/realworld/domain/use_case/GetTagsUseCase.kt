package com.walkmansit.realworld.domain.use_case

import com.walkmansit.realworld.domain.repository.ArticleRepository
import com.walkmansit.realworld.domain.util.Either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTagsUseCase @Inject constructor (
    private val articleRepository: ArticleRepository
) {
    suspend operator fun invoke(): Either<Boolean, List<String>> {
        return withContext(Dispatchers.IO) {
             when (val articleResponse = articleRepository.getTags()) {
                is Either.Fail -> Either.fail(false)
                is Either.Success -> Either.success(articleResponse.value)
            }
        }
    }
}