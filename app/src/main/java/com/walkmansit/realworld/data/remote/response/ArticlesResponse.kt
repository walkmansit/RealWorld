package com.walkmansit.realworld.data.remote.response

import com.google.gson.annotations.SerializedName
import com.walkmansit.realworld.domain.model.Article

data class ArticlesResponse(
    @SerializedName("articles")
    val articles: List<SingleArticle>,
)

fun ArticlesResponse.toDomain(): List<Article> = articles.map { it.toDomain() }
