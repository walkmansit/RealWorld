package com.walkmansit.realworld.data.repository

import com.walkmansit.realworld.data.remote.ApiService
import com.walkmansit.realworld.data.remote.response.NewArticleFailedResponse
import com.walkmansit.realworld.data.remote.response.toDomain
import com.walkmansit.realworld.data.remote.response.toNewArticleFailed
import com.walkmansit.realworld.data.util.getErrorResponse
import com.walkmansit.realworld.domain.model.Article
import com.walkmansit.realworld.domain.model.ArticleFilterType
import com.walkmansit.realworld.domain.model.ArticlesFilter
import com.walkmansit.realworld.domain.model.EditArticle
import com.walkmansit.realworld.domain.model.EditArticleFailed
import com.walkmansit.realworld.domain.model.NewArticle
import com.walkmansit.realworld.domain.model.NewArticleFailed
import com.walkmansit.realworld.domain.model.RequestFailed
import com.walkmansit.realworld.domain.model.toNetworkRequest
import com.walkmansit.realworld.domain.repository.ArticleRepository
import com.walkmansit.realworld.domain.util.Either
import retrofit2.HttpException
import java.io.IOException

class ArticleRepositoryImpl(
    private val apiService: ApiService,
) : ArticleRepository {
    override suspend fun getArticle(slug: String): Either<RequestFailed, Article> =
        try {
            val response = apiService.getArticle(slug)
            Either.success(response.toDomain())
        } catch (e: IOException) {
            Either.fail(RequestFailed(commonError = e.message.orEmpty()))
        } catch (e: HttpException) {
            Either.fail(RequestFailed(commonError = e.message.orEmpty()))
        }

    override suspend fun getArticles(filter: ArticlesFilter): Either<RequestFailed, List<Article>> =
        try {
            val response =
                if (filter.filterType != ArticleFilterType.Feed) {
                    apiService.getArticles(
                        filter.tag,
                        filter.author,
                        filter.favorited,
                        filter.limit,
                        filter.offset,
                    )
                } else {
                    apiService.getArticlesFeed(
                        filter.limit,
                        filter.offset,
                    )
                }

            Either.success(response.toDomain())
        } catch (e: IOException) {
            Either.fail(RequestFailed(commonError = e.message.orEmpty()))
        } catch (e: HttpException) {
            Either.fail(RequestFailed(commonError = e.message.orEmpty()))
        }

    override suspend fun getArticlesFeed(filter: ArticlesFilter): Either<RequestFailed, List<Article>> =
        try {
            val response = apiService.getArticlesFeed(filter.limit, filter.offset)
            Either.success(response.toDomain())
        } catch (e: IOException) {
            Either.fail(RequestFailed(commonError = e.message.orEmpty()))
        } catch (e: HttpException) {
            Either.fail(RequestFailed(commonError = e.message.orEmpty()))
        }

    override suspend fun updateArticle(
        editArticle: EditArticle,
        originalArticle: Article,
    ): Either<EditArticleFailed, Article> {
        TODO("Not yet implemented")
    }

    override suspend fun createArticle(newArticle: NewArticle): Either<NewArticleFailed, Article> =
        try {
            val response = apiService.createArticle(newArticle.toNetworkRequest())
            Either.success(response.toDomain())
        } catch (e: IOException) {
            Either.fail(NewArticleFailed(commonError = e.message.orEmpty()))
        } catch (e: HttpException) {
            val errorResponse = getErrorResponse<NewArticleFailedResponse>(e.response()?.errorBody()?.string() ?: "")
            Either.fail(errorResponse.toNewArticleFailed())
        }

    override suspend fun getTags(): Either<RequestFailed, List<String>> =
        try {
            val response = apiService.getTags()
            Either.success(response.toDomain())
        } catch (e: IOException) {
            Either.fail(RequestFailed(commonError = e.message.orEmpty()))
        } catch (e: HttpException) {
            Either.fail(RequestFailed(commonError = e.message.orEmpty()))
        }
}
