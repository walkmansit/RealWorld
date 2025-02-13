package com.walkmansit.realworld.data.remote.request

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    @SerializedName("user")
    var user: UserAuthRequest
)

data class UserAuthRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
)