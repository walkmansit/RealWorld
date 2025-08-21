package com.walkmansit.realworld.data.remote

import com.walkmansit.realworld.data.remote.request.AuthRequest
import com.walkmansit.realworld.data.remote.request.NewArticleRequest
import com.walkmansit.realworld.data.remote.request.RegistrationRequest
import com.walkmansit.realworld.data.remote.response.ArticlesResponse
import com.walkmansit.realworld.data.remote.response.AuthResponse
import com.walkmansit.realworld.data.remote.response.ProfileResponse
import com.walkmansit.realworld.data.remote.response.SingleArticleResponse
import com.walkmansit.realworld.data.remote.response.TagsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

@Suppress("SpellCheckingInspection")
interface ApiService {


    //Authorization
    @POST("/api/v1/users/login")
    suspend fun loginUser(
        @Body loginRequest: AuthRequest
    ): AuthResponse


    @POST("/api/v1/users")
    suspend fun registerUser(
        @Body registerRequest: RegistrationRequest
    ): AuthResponse

    //Tags
    @GET("/api/v1/tags")
    suspend fun getTags(): TagsResponse

    //Profile
    @GET("/api/v1/profiles/{username}")
    suspend fun getProfile(
        @Path("username") username: String,
    ): ProfileResponse

    //Articles

    @GET("/api/v1/articles/{slug}")
    suspend fun getArticle(@Path("slug") slug: String,
    ): SingleArticleResponse

    @POST("/api/v1/articles")
    suspend fun createArticle(
        @Body newArticle: NewArticleRequest
    ): SingleArticleResponse

    @GET("/api/v1/articles")
    suspend fun getArticles(
        @Query("tag") tag: String?,
        @Query("author") author: String?,
        @Query("favorited") favorited: String?,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        ): ArticlesResponse

    @GET("/api/v1/articles/feed")
    suspend fun getArticlesFeed(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): ArticlesResponse
}