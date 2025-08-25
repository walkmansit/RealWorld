package com.walkmansit.realworld.data.model.response

import com.walkmansit.realworld.domain.model.LoginFailed

data class LoginFailedResponse(
    val user: LoginErrorBody,
)

data class LoginErrorBody(
    val email: List<String> = listOf(),
    val password: List<String> = listOf(),
)

fun LoginFailedResponse.toLoginFailed() =
    LoginFailed(
        emailError = user.email.joinToString(", "),
        passwordError = user.password.joinToString(", "),
    )
