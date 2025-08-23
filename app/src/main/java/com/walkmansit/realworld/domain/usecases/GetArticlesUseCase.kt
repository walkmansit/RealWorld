package com.walkmansit.realworld.domain.usecases

import com.walkmansit.realworld.domain.model.Article
import com.walkmansit.realworld.domain.model.ArticlesFilter
import com.walkmansit.realworld.domain.repository.ArticleRepository
import com.walkmansit.realworld.domain.util.Either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetArticlesUseCase
    @Inject
    constructor(
        private val articleRepository: ArticleRepository,
    ) {
        suspend operator fun invoke(filter: ArticlesFilter): Either<Boolean, List<Article>> =
            withContext(Dispatchers.IO) {
                when (val articlesResponse = articleRepository.getArticles(filter)) {
                    is Either.Fail -> Either.fail(false)
                    is Either.Success -> articlesResponse
                }
            }
    }
