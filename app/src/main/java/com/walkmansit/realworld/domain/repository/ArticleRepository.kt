package com.walkmansit.realworld.domain.repository

import com.walkmansit.realworld.domain.model.Article
import com.walkmansit.realworld.domain.model.NewArticle
import com.walkmansit.realworld.domain.model.NewArticleFailed
import com.walkmansit.realworld.domain.model.ProfileFailed
import com.walkmansit.realworld.domain.util.Either

interface ArticleRepository {
    suspend fun createArticle(newArticle: NewArticle): Either<NewArticleFailed, Article>
    suspend fun getTags(): Either<ProfileFailed, List<String>>
}