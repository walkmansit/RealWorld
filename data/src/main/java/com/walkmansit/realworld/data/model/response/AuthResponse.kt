package com.walkmansit.realworld.data.model.response

import com.google.gson.annotations.SerializedName
import com.walkmansit.realworld.domain.model.User

data class AuthResponse(
    @SerializedName("user")
    var userResponse: AuthUserResponse,
)

data class AuthUserResponse(
    @SerializedName("email")
    val email: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("bio")
    val bio: String,
    @SerializedName("image")
    val image: String?,
)

fun AuthResponse.toDomain() =
    User(
        email = userResponse.email,
        token = userResponse.token,
        username = userResponse.username,
        bio = userResponse.bio,
        image = userResponse.image,
    )
