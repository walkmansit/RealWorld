package com.walkmansit.realworld.domain.model

data class Profile(
    val username: String,
    val bio: String,
    val image: String?,
    val following: Boolean,
)
