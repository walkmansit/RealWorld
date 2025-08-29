package com.walkmansit.realworld.presenter.models

import com.walkmansit.realworld.domain.model.Author
import com.walkmansit.realworld.domain.util.ModelsMapper

class AuthorDomainToUIMapper : ModelsMapper<Author, AuthorUI> {
    override fun map(data: Author): AuthorUI =
        AuthorUI(
            username = data.username,
            bio = data.bio,
            image = data.image,
            following = data.following,
        )
}
