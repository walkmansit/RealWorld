package com.walkmansit.realworld.data.repository

import com.walkmansit.realworld.data.model.request.AuthRequest
import com.walkmansit.realworld.data.model.request.NewArticleBody
import com.walkmansit.realworld.data.model.request.NewArticleRequest
import com.walkmansit.realworld.data.model.request.RegistrationRequest
import com.walkmansit.realworld.data.model.request.UserAuthRequest
import com.walkmansit.realworld.data.model.request.UserRegistrationRequest
import com.walkmansit.realworld.domain.model.NewArticle
import com.walkmansit.realworld.domain.model.UserLoginCredentials
import com.walkmansit.realworld.domain.model.UserRegisterCredentials

fun NewArticle.toNetworkRequest() =
    NewArticleRequest(
        article =
            NewArticleBody(
                title = title,
                description = description,
                body = body,
                tags = tags,
            ),
    )

fun UserLoginCredentials.toNetworkRequest() =
    AuthRequest(
        user =
            UserAuthRequest(
                email = email,
                password = password,
            ),
    )

fun UserRegisterCredentials.toNetworkRequest() =
    RegistrationRequest(
        user =
            UserRegistrationRequest(
                username = username,
                email = email,
                password = password,
            ),
    )


