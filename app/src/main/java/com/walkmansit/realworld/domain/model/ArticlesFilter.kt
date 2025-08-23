package com.walkmansit.realworld.domain.model

@Suppress("SpellCheckingInspection")
data class ArticlesFilter(
    var filterType: ArticleFilterType,
    var tag: String? = null,
    var author: String? = null,
    var favorited: String? = null,
    var limit: Int = 10,
    var offset: Int = 0,
)

enum class ArticleFilterType {
    Feed,
    MyArticles,
    Explore,
    ;

    fun toArticlesFilter(username: String): ArticlesFilter =
        when (this) {
            Feed -> ArticlesFilter(this)
            MyArticles -> ArticlesFilter(this, author = username)
            Explore -> ArticlesFilter(this)
        }
}
