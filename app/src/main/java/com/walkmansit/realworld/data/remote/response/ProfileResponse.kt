package com.walkmansit.realworld.data.remote.response

import com.google.gson.annotations.SerializedName
import com.walkmansit.realworld.domain.model.Profile

data class ProfileResponse(
    @SerializedName("profile")
    var profile: UserProfile,
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

fun ProfileResponse.toDomain() =
    Profile(
        username = profile.username,
        bio = profile.bio,
        image = profile.image,
        following = profile.following,
    )
