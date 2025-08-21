package com.walkmansit.realworld.data.remote.response

data class LoginErrorResponse(
    val user: LoginErrorBody,
)

data class LoginErrorBody(
    val email: List<String> = listOf(),
    val password: List<String> = listOf(),
)
