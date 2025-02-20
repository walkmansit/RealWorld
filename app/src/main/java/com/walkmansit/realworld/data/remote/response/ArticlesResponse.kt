package com.walkmansit.realworld.data.remote.response

import com.google.gson.annotations.SerializedName
import java.util.Date


data class ArticlesResponse(
    val articles: List<SingleArticleResponse>
)

data class SingleArticleResponse(
    @SerializedName("article")
    val article: SingleArticle
)


data class SingleArticle(
    @SerializedName("slug")
    val slug: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
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

data class AuthorResponse(
    @SerializedName("username")
    val username: String,
    @SerializedName("bio")
    val bio: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("following")
    val following: String,
)