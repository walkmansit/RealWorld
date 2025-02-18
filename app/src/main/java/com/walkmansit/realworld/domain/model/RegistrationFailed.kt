package com.walkmansit.realworld.domain.model

data class RegistrationFailed(
    val usernameError: String? = null,
    val passwordError: String? = null,
    val emailError: String? = null,
    val commonError: String? = null,
)