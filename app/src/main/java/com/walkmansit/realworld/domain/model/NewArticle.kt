package com.walkmansit.realworld.domain.model

import com.walkmansit.realworld.data.remote.request.NewArticleBody
import com.walkmansit.realworld.data.remote.request.NewArticleRequest

data class NewArticle(
    val title: String,
    val description: String,
    val body: String,
    val tags: List<String> = listOf(),
)

data class NewArticleFailed(
    val titleError: String? = null,
    val descriptionError: String? = null,
    val bodyError: String? = null,
    val commonError: String? = null,
)

fun NewArticle.toNetworkRequest() =
    NewArticleRequest(
        article =
            NewArticleBody(
                title = title,
                description = description,
                body = body,
                tags = tags,
            ),
    )
