package com.walkmansit.realworld.data.remote.response

import com.walkmansit.realworld.domain.model.RegistrationFailed

data class RegistrationFailedResponse(
    val user: RegistrationErrorBody,
)

data class RegistrationErrorBody(
    val username: List<String> = listOf(),
    val email: List<String> = listOf(),
    val password: List<String> = listOf(),
)

fun RegistrationFailedResponse.toRegistrationFailed() =
    RegistrationFailed(
        usernameError = user.username.joinToString(", "),
        emailError = user.email.joinToString(", "),
        passwordError = user.password.joinToString(", "),
    )
