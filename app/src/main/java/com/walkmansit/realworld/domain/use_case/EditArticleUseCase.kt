package com.walkmansit.realworld.domain.use_case

import com.walkmansit.realworld.domain.model.Article
import com.walkmansit.realworld.domain.model.EditArticle
import com.walkmansit.realworld.domain.model.EditArticleFailed
import com.walkmansit.realworld.domain.model.NewArticle
import com.walkmansit.realworld.domain.model.NewArticleFailed
import com.walkmansit.realworld.domain.repository.ArticleRepository
import com.walkmansit.realworld.domain.util.Either
import javax.inject.Inject

class EditArticleUseCase @Inject constructor(
    private val repository: ArticleRepository,
) {
    suspend operator fun invoke(
        editArticle: EditArticle,
        originalArticle: Article,
    ): Either<EditArticleFailed, Article> {

        val titleError = if (editArticle.title.isBlank()) "Title cannot be blank" else null
        val descriptionError = if (editArticle.description.isBlank()) "Description cannot be blank" else null
        val bodyError = if (editArticle.body.isBlank()) "Body cannot be blank" else null

        return if (titleError == null && descriptionError == null && bodyError == null) {
            repository.updateArticle(editArticle, originalArticle)
        } else
            Either.fail(EditArticleFailed(titleError, descriptionError, bodyError))
    }
}