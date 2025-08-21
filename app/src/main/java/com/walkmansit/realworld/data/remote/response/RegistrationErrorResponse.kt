package com.walkmansit.realworld.data.remote.response

data class RegistrationErrorResponse(
    val error: String?,
    val user: RegistrationErrorBody,
)

data class RegistrationErrorBody(
    val username: List<String> = listOf(),
    val email: List<String> = listOf(),
    val password: List<String> = listOf(),
)
