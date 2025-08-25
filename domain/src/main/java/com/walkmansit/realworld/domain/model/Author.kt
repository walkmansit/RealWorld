package com.walkmansit.realworld.domain.model

data class Author(
    val username: String,
    val bio: String,
    val image: String,
    val following: Boolean,
)
