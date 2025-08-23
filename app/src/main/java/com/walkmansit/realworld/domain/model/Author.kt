package com.walkmansit.realworld.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Author(
    val username: String,
    val bio: String,
    val image: String,
    val following: Boolean,
) : Parcelable
