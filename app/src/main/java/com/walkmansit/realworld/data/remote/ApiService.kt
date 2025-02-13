package com.walkmansit.realworld.data.remote

import com.walkmansit.realworld.data.remote.request.AuthRequest
import com.walkmansit.realworld.data.remote.request.RegistrationRequest
import com.walkmansit.realworld.data.remote.response.AuthResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("/api/users/login")
    suspend fun loginUser(
        @Body loginRequest: AuthRequest
    ) : AuthResponse


    @POST("/api/users")
    suspend fun registerUser(
        @Body registerRequest: RegistrationRequest
    ) : AuthResponse
}