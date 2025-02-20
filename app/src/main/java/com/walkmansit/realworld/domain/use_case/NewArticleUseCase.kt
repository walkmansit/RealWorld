package com.walkmansit.realworld.domain.use_case

import com.walkmansit.realworld.domain.model.Article
import com.walkmansit.realworld.domain.model.NewArticle
import com.walkmansit.realworld.domain.model.NewArticleFailed
import com.walkmansit.realworld.domain.repository.ArticleRepository
import com.walkmansit.realworld.domain.util.Either
import javax.inject.Inject

class NewArticleUseCase @Inject constructor(
    private val repository: ArticleRepository,
) {
    suspend operator fun invoke(
        title: String,
        description: String,
        body: String,
        tags: List<String>,
    ): Either<NewArticleFailed, Article> {

        val titleError = if (title.isBlank()) "Title cannot be blank" else null
        val descriptionError = if (description.isBlank()) "Description cannot be blank" else null
        val bodyError = if (body.isBlank()) "Body cannot be blank" else null

        return if (titleError == null && descriptionError == null && bodyError == null) {
            repository.createArticle(NewArticle(title, description, body, tags))
        } else
            Either.fail(NewArticleFailed(titleError, descriptionError, bodyError))
    }
}