package com.walkmansit.realworld.data.remote

import com.walkmansit.realworld.data.remote.request.AuthRequest
import com.walkmansit.realworld.data.remote.request.NewArticleRequest
import com.walkmansit.realworld.data.remote.request.RegistrationRequest
import com.walkmansit.realworld.data.remote.response.AuthResponse
import com.walkmansit.realworld.data.remote.response.ProfileResponse
import com.walkmansit.realworld.data.remote.response.SingleArticleResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    //Profile
    @GET("/api/profiles/{username}")
    suspend fun getProfile(
        @Path("username") username: String,
    ): ProfileResponse

    //Articles
    @POST("/api/articles")
    suspend fun createArticle(
        @Body newArticle: NewArticleRequest
    ): SingleArticleResponse
}