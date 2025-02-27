package com.walkmansit.realworld.domain.use_case

import com.walkmansit.realworld.domain.model.Article
import com.walkmansit.realworld.domain.repository.ArticleRepository
import com.walkmansit.realworld.domain.util.Either
import javax.inject.Inject

class GetArticleUseCase @Inject constructor (
    private val articleRepository: ArticleRepository
) {
    suspend operator fun invoke(slug: String): Either<Boolean, Article> {
        return when(val articleResponse = articleRepository.getArticle(slug)){
            is Either.Fail -> Either.fail(false)
            is Either.Success -> articleResponse
        }
    }
}