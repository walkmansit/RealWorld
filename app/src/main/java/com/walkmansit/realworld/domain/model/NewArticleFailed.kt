package com.walkmansit.realworld.domain.model

data class NewArticleFailed(
    val titleError: String? = null,
    val descriptionError: String? = null,
    val bodyError: String? = null,
    val commonError: String? = null,
)