package com.walkmansit.realworld.data.model.response

import com.google.gson.annotations.SerializedName

data class TagsResponse(
    @SerializedName("tags")
    var tags: List<String>,
)

fun TagsResponse.toDomain() = tags
