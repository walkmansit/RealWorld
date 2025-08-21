package com.walkmansit.realworld.data.util

import com.google.gson.Gson
import com.walkmansit.realworld.data.remote.request.AuthRequest
import com.walkmansit.realworld.data.remote.request.NewArticleBody
import com.walkmansit.realworld.data.remote.request.NewArticleRequest
import com.walkmansit.realworld.data.remote.request.RegistrationRequest
import com.walkmansit.realworld.data.remote.request.UserAuthRequest
import com.walkmansit.realworld.data.remote.request.UserRegistrationRequest
import com.walkmansit.realworld.data.remote.response.ArticlesResponse
import com.walkmansit.realworld.data.remote.response.AuthResponse
import com.walkmansit.realworld.data.remote.response.AuthorResponse
import com.walkmansit.realworld.data.remote.response.BaseErrorResponse
import com.walkmansit.realworld.data.remote.response.LoginErrorResponse
import com.walkmansit.realworld.data.remote.response.NewArticleErrorResponse
import com.walkmansit.realworld.data.remote.response.ProfileResponse
import com.walkmansit.realworld.data.remote.response.RegistrationErrorResponse
import com.walkmansit.realworld.data.remote.response.SingleArticle
import com.walkmansit.realworld.data.remote.response.SingleArticleResponse
import com.walkmansit.realworld.data.remote.response.TagsResponse
import com.walkmansit.realworld.domain.model.Article
import com.walkmansit.realworld.domain.model.Author
import com.walkmansit.realworld.domain.model.CommonError
import com.walkmansit.realworld.domain.model.LoginFailed
import com.walkmansit.realworld.domain.model.NewArticle
import com.walkmansit.realworld.domain.model.NewArticleFailed
import com.walkmansit.realworld.domain.model.Profile
import com.walkmansit.realworld.domain.model.RegistrationFailed
import com.walkmansit.realworld.domain.model.User
import com.walkmansit.realworld.domain.model.UserLoginCredentials
import com.walkmansit.realworld.domain.model.UserRegisterCredentials
import com.walkmansit.realworld.domain.util.Either

inline fun <reified T> getErrorResponse(body: String) : T {
    return Gson().fromJson(
        body, T::class.java
    )
}

inline fun <reified R, T> getErrorEither(body: String, mapper: ModelsMapper<R, T>) : Either<CommonError, T> {
    return try {
        val result = getErrorResponse<BaseErrorResponse>(body)
        Either.fail(CommonError(result.error))
    }
    catch (e: Exception) {
        val result = getErrorResponse<R>(body)
        Either.success(mapper.map(result))
    }
}

interface ModelsMapper<R, T> {
    fun map(data: R) : T
}

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

fun RegistrationErrorResponse.toRegistrationFailed() = RegistrationFailed(
    usernameError = user.username.joinToString(", "),
    emailError = user.email.joinToString(", "),
    passwordError = user.password.joinToString(", "),
)

fun LoginErrorResponse.toLoginFailed() = LoginFailed(
    emailError = user.email.joinToString(", "),
    passwordError = user.password.joinToString(", "),
)

fun NewArticleErrorResponse.toNewArticleFailed() = NewArticleFailed(
    titleError = errors.title.joinToString(", "),
    descriptionError = errors.description.joinToString(", "),
    bodyError = errors.body.joinToString(", "),
)

fun NewArticle.toNetworkRequest() = NewArticleRequest(
    article = NewArticleBody(
        title = title,
        description = description,
        body = body,
        tags = tags,
    )
)

fun SingleArticle.toDomain() = Article(
    slug = slug,
    title = title,
    description = description,
    body = body,
    tagList = tagList,
    createdAt = createdAt,
    updatedAt = updatedAt,
    favorited = favorited,
    favoritesCount = favoritesCount,
    author = author.toDomain(),
)


fun SingleArticleResponse.toDomain() = Article(
    slug = article.slug,
    title = article.title,
    description = article.description,
    body = article.body,
    tagList = article.tagList,
    createdAt = article.createdAt,
    updatedAt = article.updatedAt,
    favorited = article.favorited,
    favoritesCount = article.favoritesCount,
    author = article.author.toDomain(),
)

fun AuthorResponse.toDomain() = Author(
    username = username,
    bio = bio ?: "",
    image = image ?: "",
    following = following,
)
fun ArticlesResponse.toDomain() : List<Article> =
    articles.map{ it.toDomain() }

fun TagsResponse.toDomain() = tags