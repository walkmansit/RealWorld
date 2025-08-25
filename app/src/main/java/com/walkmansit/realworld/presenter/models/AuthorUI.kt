package com.walkmansit.realworld.presenter.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthorUI(
    val username: String,
    val bio: String,
    val image: String,
    val following: Boolean,
) : Parcelable
