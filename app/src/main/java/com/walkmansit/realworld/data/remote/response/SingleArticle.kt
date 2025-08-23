package com.walkmansit.realworld.data.remote.response

import com.google.gson.annotations.SerializedName
import com.walkmansit.realworld.domain.model.Article
import java.util.Date

@Suppress("SpellCheckingInspection")
data class SingleArticle(
    @SerializedName("slug")
    val slug: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("body")
    val body: String?,
    @SerializedName("tagList")
    val tagList: List<String>,
    @SerializedName("createdAt")
    val createdAt: Date,
    @SerializedName("updatedAt")
    val updatedAt: Date,
    @SerializedName("favorited")
    val favorited: Boolean,
    @SerializedName("favoritesCount")
    val favoritesCount: Int,
    @SerializedName("author")
    val author: AuthorResponse,
)

fun SingleArticle.toDomain() =
    Article(
        slug = slug,
        title = title,
        description = description,
        body = body,
        tagList = tagList,
        createdAt = createdAt,
        updatedAt = updatedAt,
        favorited = favorited,
        favoritesCount = favoritesCount,
        author = author.toDomain(),
    )
