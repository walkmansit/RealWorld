package com.walkmansit.realworld.data.remote.response

import com.google.gson.annotations.SerializedName

data class TagsResponse(
    @SerializedName("tags")
    var tags: List<String>
)
