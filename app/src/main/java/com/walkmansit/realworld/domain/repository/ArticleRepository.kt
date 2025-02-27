package com.walkmansit.realworld.domain.repository

import com.walkmansit.realworld.domain.model.Article
import com.walkmansit.realworld.domain.model.ArticlesFilter
import com.walkmansit.realworld.domain.model.NewArticle
import com.walkmansit.realworld.domain.model.NewArticleFailed
import com.walkmansit.realworld.domain.model.RequestFailed
import com.walkmansit.realworld.domain.util.Either

interface ArticleRepository {

    suspend fun getArticle(slug: String): Either<RequestFailed, Article>
    suspend fun getArticles(filter: ArticlesFilter): Either<RequestFailed, List<Article>>
    suspend fun getArticlesFeed(filter: ArticlesFilter): Either<RequestFailed, List<Article>>
    suspend fun createArticle(newArticle: NewArticle): Either<NewArticleFailed, Article>
    suspend fun getTags(): Either<RequestFailed, List<String>>
}