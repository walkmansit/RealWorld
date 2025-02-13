package com.walkmansit.realworld.domain.model

data class LoginFailed (
    val passwordError: String? = null,
    val emailError : String? = null,
    val commonError : String? = null,
)