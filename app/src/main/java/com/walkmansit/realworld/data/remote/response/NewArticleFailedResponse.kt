package com.walkmansit.realworld.data.remote.response

data class NewArticleFailedResponse(
    val errors: NewArticleErrorBody,
)

data class NewArticleErrorBody(
    val title: List<String> = listOf(),
    val description: List<String> = listOf(),
    val body: List<String> = listOf(),
)