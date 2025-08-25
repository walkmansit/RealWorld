package com.walkmansit.realworld.presenter.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Suppress("SpellCheckingInspection")
@Parcelize
data class ArticleUI(
    val slug: String,
    val title: String,
    val description: String,
    val body: String?,
    val tagList: List<String>,
    val createdAt: Date,
    val updatedAt: Date,
    val favorited: Boolean,
    val favoritesCount: Int,
    val author: AuthorUI,
) : Parcelable
