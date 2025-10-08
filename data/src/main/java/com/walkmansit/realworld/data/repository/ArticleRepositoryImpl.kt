package com.walkmansit.realworld.data.repository

import com.walkmansit.realworld.data.api.ApiService
import com.walkmansit.realworld.data.model.response.toDomain
import com.walkmansit.realworld.data.util.safeApiCall
import com.walkmansit.realworld.domain.model.Article
import com.walkmansit.realworld.domain.model.ArticleFilterType
import com.walkmansit.realworld.domain.model.ArticlesFilter
import com.walkmansit.realworld.domain.model.EditArticle
import com.walkmansit.realworld.domain.model.NewArticle
import com.walkmansit.realworld.domain.model.RequestFailed
import com.walkmansit.realworld.domain.repository.ArticleRepository
import com.walkmansit.realworld.domain.util.Either
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : ArticleRepository {

    override suspend fun getArticle(slug: String): Either<RequestFailed, Article> =
        safeApiCall(block = { apiService.getArticle(slug).toDomain() })

    override suspend fun getArticles(filter: ArticlesFilter): Either<RequestFailed, List<Article>> =
        safeApiCall(block =  {
            val response = if (filter.filterType != ArticleFilterType.Feed) {
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
            response.toDomain()
        })

    override suspend fun getArticlesFeed(filter: ArticlesFilter): Either<RequestFailed, List<Article>> =
        safeApiCall(block = {
            apiService.getArticlesFeed(filter.limit, filter.offset).toDomain()
        })

    override suspend fun updateArticle(
        editArticle: EditArticle,
        originalArticle: Article,
    ): Either<RequestFailed, Article> {
        TODO("Not yet implemented")
    }

    override suspend fun createArticle(newArticle: NewArticle): Either<RequestFailed, Article> =
        safeApiCall(block = {
            apiService.createArticle(newArticle.toNetworkRequest()).toDomain()
        })


    override suspend fun getTags(): Either<RequestFailed, List<String>> =
        safeApiCall(block = {
            apiService.getTags().toDomain()
        })
}
