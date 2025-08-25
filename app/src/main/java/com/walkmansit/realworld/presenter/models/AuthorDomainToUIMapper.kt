package com.walkmansit.realworld.presenter.models

import com.walkmansit.realworld.domain.model.Author
import com.walkmansit.realworld.domain.util.ModelsMapper

class AuthorDomainToUIMapper : ModelsMapper<Author, AuthorUI> {
    override fun map(author: Author): AuthorUI =
        AuthorUI(
            username = author.username,
            bio = author.bio,
            image = author.image,
            following = author.following,
        )
}
