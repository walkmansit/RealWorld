package com.walkmansit.realworld.data.repository

import com.walkmansit.realworld.data.remote.ApiService
import com.walkmansit.realworld.data.remote.response.NewArticleErrorResponse
import com.walkmansit.realworld.data.util.getErrorResponse
import com.walkmansit.realworld.data.util.toDomain
import com.walkmansit.realworld.data.util.toNetworkRequest
import com.walkmansit.realworld.data.util.toNewArticleFailed
import com.walkmansit.realworld.domain.model.Article
import com.walkmansit.realworld.domain.model.NewArticle
import com.walkmansit.realworld.domain.model.NewArticleFailed
import com.walkmansit.realworld.domain.model.ProfileFailed
import com.walkmansit.realworld.domain.repository.ArticleRepository
import com.walkmansit.realworld.domain.util.Either
import retrofit2.HttpException
import java.io.IOException

class ArticleRepositoryImpl(
    private val apiService: ApiService
) : ArticleRepository {
    override suspend fun createArticle(newArticle: NewArticle): Either<NewArticleFailed, Article> {
        return try {
            val response = apiService.createArticle(newArticle.toNetworkRequest())
            Either.success(response.toDomain())
        } catch (e: IOException) {
            Either.fail(NewArticleFailed(commonError = e.message.orEmpty()))
        } catch (e: HttpException) {
            val errorResponse = getErrorResponse<NewArticleErrorResponse>(e.response()?.errorBody()?.string() ?: "")
            Either.fail(errorResponse.toNewArticleFailed())
        }
    }

    override suspend fun getTags(): Either<ProfileFailed, List<String>> {
        return try {
            val response = apiService.getTags()
            Either.success(response.toDomain())
        } catch (e: IOException) {
            Either.fail(ProfileFailed(commonError = e.message.orEmpty()))
        } catch (e: HttpException) {
            Either.fail(ProfileFailed(commonError = e.message.orEmpty()))
        }
    }
}