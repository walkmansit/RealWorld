package com.walkmansit.realworld.data.remote.response

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("user")
    var userResponse: AuthUserResponse
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