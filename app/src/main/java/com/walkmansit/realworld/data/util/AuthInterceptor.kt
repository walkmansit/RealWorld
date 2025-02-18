package com.walkmansit.realworld.data.util

import com.walkmansit.realworld.domain.repository.TokenRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenRepository: TokenRepository,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            tokenRepository.getToken()
        }
        val request = chain.request().newBuilder()
        request.addHeader("Authorization", "Token $token")
        return chain.proceed(request.build())
    }
}