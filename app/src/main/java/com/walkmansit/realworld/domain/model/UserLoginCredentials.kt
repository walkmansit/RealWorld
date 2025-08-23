package com.walkmansit.realworld.domain.model

import com.walkmansit.realworld.data.remote.request.AuthRequest
import com.walkmansit.realworld.data.remote.request.UserAuthRequest

data class UserLoginCredentials(
    var email: String,
    var password: String,
)

fun UserLoginCredentials.toNetworkRequest() =
    AuthRequest(
        user =
            UserAuthRequest(
                email = email,
                password = password,
            ),
    )
