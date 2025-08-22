package com.walkmansit.realworld.data.remote.response

data class RegistrationFailedResponse(
    val user: RegistrationErrorBody,
)

data class RegistrationErrorBody(
    val username: List<String> = listOf(),
    val email: List<String> = listOf(),
    val password: List<String> = listOf(),
)
