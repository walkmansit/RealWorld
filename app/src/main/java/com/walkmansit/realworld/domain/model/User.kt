package com.walkmansit.realworld.domain.model

data class User(
    val email: String,
    val token: String,
    val username: String,
    val bio: String,
    val image: String?,
)