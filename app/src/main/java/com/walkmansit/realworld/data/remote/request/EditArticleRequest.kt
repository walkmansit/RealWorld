package com.walkmansit.realworld.data.remote.request

import com.google.gson.annotations.SerializedName

class EditArticleRequest(
    @SerializedName("article")
    var article: EditArticleBody
)

data class EditArticleBody(
    @SerializedName("title")
    var title: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("body")
    var body: String?,
)