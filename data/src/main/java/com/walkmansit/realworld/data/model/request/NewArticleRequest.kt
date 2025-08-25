package com.walkmansit.realworld.data.model.request

import com.google.gson.annotations.SerializedName

class NewArticleRequest(
    @SerializedName("article")
    var article: NewArticleBody,
)

data class NewArticleBody(
    @SerializedName("title")
    var title: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("body")
    var body: String,
    @SerializedName("tagList")
    var tags: List<String>,
)
