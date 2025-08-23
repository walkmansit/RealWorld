package com.walkmansit.realworld.domain.model

import com.walkmansit.realworld.data.remote.request.RegistrationRequest
import com.walkmansit.realworld.data.remote.request.UserRegistrationRequest

data class UserRegisterCredentials(
    var username: String,
    var email: String,
    var password: String,
)

fun UserRegisterCredentials.toNetworkRequest() =
    RegistrationRequest(
        user =
            UserRegistrationRequest(
                username = username,
                email = email,
                password = password,
            ),
    )
