package com.walkmansit.realworld.data

import com.walkmansit.realworld.data.remote.request.AuthRequest
import com.walkmansit.realworld.data.remote.request.NewArticleBody
import com.walkmansit.realworld.data.remote.request.NewArticleRequest
import com.walkmansit.realworld.data.remote.request.RegistrationRequest
import com.walkmansit.realworld.data.remote.request.UserAuthRequest
import com.walkmansit.realworld.data.remote.request.UserRegistrationRequest
import com.walkmansit.realworld.data.remote.response.AuthResponse
import com.walkmansit.realworld.data.remote.response.AuthorResponse
import com.walkmansit.realworld.data.remote.response.ProfileResponse
import com.walkmansit.realworld.data.remote.response.SingleArticleResponse
import com.walkmansit.realworld.data.remote.response.TagsResponse
import com.walkmansit.realworld.data.repository.ErrorResponse
import com.walkmansit.realworld.domain.model.Article
import com.walkmansit.realworld.domain.model.Author
import com.walkmansit.realworld.domain.model.NewArticle
import com.walkmansit.realworld.domain.model.Profile
import com.walkmansit.realworld.domain.model.RegistrationFailed
import com.walkmansit.realworld.domain.model.User
import com.walkmansit.realworld.domain.model.UserLoginCredentials
import com.walkmansit.realworld.domain.model.UserRegisterCredentials

fun AuthResponse.toDomain() = User(
    email = userResponse.email,
    token = userResponse.token,
    username = userResponse.username,
    bio = userResponse.bio,
    image = userResponse.image,
)

fun UserLoginCredentials.toNetworkRequest() = AuthRequest(
    user = UserAuthRequest(
        email = email,
        password = password,
    )
)

fun UserRegisterCredentials.toNetworkRequest() = RegistrationRequest(
    user = UserRegistrationRequest(
        username = username,
        email = email,
        password = password,
    )
)

fun ProfileResponse.toDomain() = Profile(
    username = profile.username,
    bio = profile.bio,
    image = profile.image,
    following = profile.following,
)

fun ErrorResponse.toRegistrationFailed() = RegistrationFailed(
    usernameError = if (type == "UserNameAlreadyTakenException") message else "",
    emailError = if (type == "EmailAlreadyTakenException") message else "",
)

fun NewArticle.toNetworkRequest() = NewArticleRequest(
    article = NewArticleBody(
        title = title,
        description = description,
        body = body,
        tags = tags,
    )
)

fun SingleArticleResponse.toDomain() = Article(
    slug = article.slug,
    title = article.title,
    description = article.description,
    tagList = article.tagList,
    createdAt = article.createdAt,
    updatedAt = article.updatedAt,
    favorited = article.favorited,
    favoritesCount = article.favoritesCount,
    author = article.author.toDomain(),
)

fun AuthorResponse.toDomain() = Author(
    username = username,
    bio = bio,
    image = image,
    following = following,
)

fun TagsResponse.toDomain() = tags