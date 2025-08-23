package com.walkmansit.realworld.domain.model

class ModelMapping {
    fun Article.toEditArticle() =
        EditArticle(
            title = title,
            description = description,
            body = body!!,
        )
}
