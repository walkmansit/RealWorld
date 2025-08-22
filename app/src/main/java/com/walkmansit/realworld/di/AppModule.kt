package com.walkmansit.realworld.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.walkmansit.realworld.BuildConfig
import com.walkmansit.realworld.data.remote.ApiService
import com.walkmansit.realworld.data.repository.ArticleRepositoryImpl
import com.walkmansit.realworld.data.repository.AuthRepositoryImpl
import com.walkmansit.realworld.data.repository.TokenRepositoryImpl
import com.walkmansit.realworld.data.repository.UserPreferencesRepositoryImpl
import com.walkmansit.realworld.data.util.AuthInterceptor
import com.walkmansit.realworld.domain.repository.ArticleRepository
import com.walkmansit.realworld.domain.repository.AuthRepository
import com.walkmansit.realworld.domain.repository.TokenRepository
import com.walkmansit.realworld.domain.repository.UserPreferencesRepository
import com.walkmansit.realworld.data.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesPreferenceDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile(Constants.USER_PREFERENCES_NAME)
            }
        )


    @Provides
    @Singleton
    fun providesUserPreferencesRepository(dataStore: DataStore<Preferences>): UserPreferencesRepository =
        UserPreferencesRepositoryImpl(dataStore)

    @Provides
    @Singleton
    fun providesTokenRepository(dataStore: DataStore<Preferences>): TokenRepository =
        TokenRepositoryImpl(dataStore)

    @Singleton
    @Provides
    fun provideAuthInterceptor(tokenRepository: TokenRepository): AuthInterceptor =
        AuthInterceptor(tokenRepository)


    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            //.authenticator(authAuthenticator)
            .build()
    }

    @Provides
    @Singleton
    fun providesApiService(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesAuthRepository(apiService: ApiService): AuthRepository =
        AuthRepositoryImpl(apiService)

    @Provides
    @Singleton
    fun providesArticleRepository(apiService: ApiService): ArticleRepository =
        ArticleRepositoryImpl(apiService)

//    @Provides
//    @Singleton
//    fun providesRegistrationUseCase(repository: AuthRepository) = RegistrationUseCase(repository)

//    @Provides
//    @Singleton
//    fun providesRegistrationViewModel(authRepository: AuthRepositoryImpl: ApiService) = RegistrationViewModel(apiService)
}