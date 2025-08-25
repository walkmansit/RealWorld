package com.walkmansit.realworld.presenter.models

import com.walkmansit.realworld.domain.model.Article
import com.walkmansit.realworld.domain.util.ModelsMapper

class ArticleDomainToUIMapper : ModelsMapper<Article, ArticleUI> {
    override fun map(data: Article): ArticleUI =
        ArticleUI(
            slug = data.slug,
            title = data.title,
            description = data.description,
            body = data.body,
            tagList = data.tagList,
            createdAt = data.createdAt,
            updatedAt = data.updatedAt,
            favorited = data.favorited,
            favoritesCount = data.favoritesCount,
            author = AuthorDomainToUIMapper().map(data.author),
        )
}
