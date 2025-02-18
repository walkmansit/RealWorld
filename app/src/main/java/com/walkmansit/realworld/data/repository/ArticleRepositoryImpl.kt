package com.walkmansit.realworld.data.repository

import com.walkmansit.realworld.data.remote.ApiService
import com.walkmansit.realworld.data.toDomain
import com.walkmansit.realworld.data.toNetworkRequest
import com.walkmansit.realworld.domain.model.Article
import com.walkmansit.realworld.domain.model.NewArticle
import com.walkmansit.realworld.domain.model.NewArticleFailed
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
            Either.fail(NewArticleFailed(commonError = e.message.orEmpty()))
        }
    }
}