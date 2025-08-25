package com.walkmansit.realworld.data.model.response

import com.google.gson.annotations.SerializedName
import com.walkmansit.realworld.domain.model.Author

data class AuthorResponse(
    @SerializedName("username")
    val username: String,
    @SerializedName("bio")
    val bio: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("following")
    val following: Boolean,
)

fun AuthorResponse.toDomain() =
    Author(
        username = username,
        bio = bio ?: "",
        image = image ?: "",
        following = following,
    )
