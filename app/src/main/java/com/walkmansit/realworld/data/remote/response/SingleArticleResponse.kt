package com.walkmansit.realworld.data.remote.response

import com.google.gson.annotations.SerializedName
import com.walkmansit.realworld.domain.model.Article

data class SingleArticleResponse(
    @SerializedName("article")
    val article: SingleArticle,
)

fun SingleArticleResponse.toDomain() =
    Article(
        slug = article.slug,
        title = article.title,
        description = article.description,
        body = article.body,
        tagList = article.tagList,
        createdAt = article.createdAt,
        updatedAt = article.updatedAt,
        favorited = article.favorited,
        favoritesCount = article.favoritesCount,
        author = article.author.toDomain(),
    )
