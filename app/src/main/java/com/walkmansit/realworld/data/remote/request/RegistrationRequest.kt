package com.walkmansit.realworld.data.remote.request

import com.google.gson.annotations.SerializedName

data class RegistrationRequest(
    @SerializedName("user")
    var user: UserRegistrationRequest,
)

data class UserRegistrationRequest(
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
)
