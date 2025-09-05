package com.walkmansit.realworld.data.di

import android.util.Log
import com.walkmansit.realworld.data.BuildConfig
import com.walkmansit.realworld.data.api.ApiService
import com.walkmansit.realworld.data.util.AuthInterceptor
import com.walkmansit.realworld.domain.repository.TokenRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideAuthInterceptor(tokenRepository: TokenRepository): AuthInterceptor = AuthInterceptor(tokenRepository)

    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            // .authenticator(authAuthenticator)
            .build()
    }

    @Provides
    @Singleton
    fun providesApiService(okHttpClient: OkHttpClient): ApiService =
        Retrofit
            .Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.API_URL)
            .also { Log.d("ApiService", "Base URL: ${BuildConfig.API_URL}") }
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
}