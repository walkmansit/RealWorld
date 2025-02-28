package com.walkmansit.realworld.domain.model

class EditArticle (
    val title: String,
    val description: String,
    val body: String,
)

data class EditArticleFailed(
    val titleError: String? = null,
    val descriptionError: String? = null,
    val bodyError: String? = null,
    val commonError: String? = null,
)
