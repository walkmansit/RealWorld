package com.walkmansit.realworld.domain.model

data class NewArticle(
    val title: String,
    val description: String,
    val body: String,
    val tags: List<String> = listOf(),
)
