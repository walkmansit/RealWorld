package com.walkmansit.realworld.data.remote.response

data class RegistrationErrorResponse(
    val errors: RegistrationErrorBody,
)

data class RegistrationErrorBody(
    val username: List<String> = listOf(),
    val email: List<String> = listOf(),
    val password: List<String> = listOf(),
)
