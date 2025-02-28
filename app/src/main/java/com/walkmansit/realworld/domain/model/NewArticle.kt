package com.walkmansit.realworld.domain.model

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