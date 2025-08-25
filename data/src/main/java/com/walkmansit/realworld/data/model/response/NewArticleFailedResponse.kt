package com.walkmansit.realworld.data.model.response

import com.walkmansit.realworld.domain.model.NewArticleFailed

data class NewArticleFailedResponse(
    val errors: NewArticleErrorBody,
)

data class NewArticleErrorBody(
    val title: List<String> = listOf(),
    val description: List<String> = listOf(),
    val body: List<String> = listOf(),
)

fun NewArticleFailedResponse.toNewArticleFailed() =
    NewArticleFailed(
        titleError = errors.title.joinToString(", "),
        descriptionError = errors.description.joinToString(", "),
        bodyError = errors.body.joinToString(", "),
    )
