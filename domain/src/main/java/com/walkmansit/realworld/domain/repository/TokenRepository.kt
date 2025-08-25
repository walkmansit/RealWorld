package com.walkmansit.realworld.domain.repository

interface TokenRepository {
    suspend fun getToken(): String

    suspend fun saveToken(token: String)

    suspend fun deleteToken()
}
