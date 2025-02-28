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
    @POST("/api/users/login")
    suspend fun loginUser(
        @Body loginRequest: AuthRequest
    ): AuthResponse


    @POST("/api/users")
    suspend fun registerUser(
        @Body registerRequest: RegistrationRequest
    ): AuthResponse

    //Tags
    @GET("/api/tags")
    suspend fun getTags(): TagsResponse

    //Profile
    @GET("/api/profiles/{username}")
    suspend fun getProfile(
        @Path("username") username: String,
    ): ProfileResponse

    //Articles

    @GET("/api/articles/{slug}")
    suspend fun getArticle(@Path("slug") slug: String,
    ): SingleArticleResponse

    @POST("/api/articles")
    suspend fun createArticle(
        @Body newArticle: NewArticleRequest
    ): SingleArticleResponse

    @GET("/api/articles")
    suspend fun getArticles(
        @Query("tag") tag: String?,
        @Query("author") author: String?,
        @Query("favorited") favorited: String?,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        ): ArticlesResponse

    @GET("/api/articles/feed")
    suspend fun getArticlesFeed(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): ArticlesResponse
}