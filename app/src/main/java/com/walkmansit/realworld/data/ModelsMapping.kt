package com.walkmansit.realworld.data

import com.walkmansit.realworld.data.remote.request.AuthRequest
import com.walkmansit.realworld.data.remote.request.RegistrationRequest
import com.walkmansit.realworld.data.remote.request.UserAuthRequest
import com.walkmansit.realworld.data.remote.request.UserRegistrationRequest
import com.walkmansit.realworld.domain.model.User
import com.walkmansit.realworld.data.remote.response.AuthResponse
import com.walkmansit.realworld.domain.model.UserLoginCredentials
import com.walkmansit.realworld.domain.model.UserRegisterCredentials

fun AuthResponse.toLocal()  = User(
    email = userResponse.email,
    token = userResponse.token,
    username = userResponse.username,
    bio = userResponse.bio,
    image = userResponse.image,
)

fun UserLoginCredentials.toNetworkRequest() = AuthRequest(
    user = UserAuthRequest(
        email = email,
        password = password,
    )
)

fun UserRegisterCredentials.toNetworkRequest() = RegistrationRequest(
    user = UserRegistrationRequest(
        username = username,
        email = email,
        password = password,
    )
)