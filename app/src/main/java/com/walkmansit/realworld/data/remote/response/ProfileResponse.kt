package com.walkmansit.realworld.data.remote.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("profile")
    var profile: UserProfile
)

data class UserProfile(
    @SerializedName("username")
    val username: String,
    @SerializedName("bio")
    val bio: String,
    @SerializedName("image")
    val image: String?,
    @SerializedName("following")
    val following: Boolean,
)