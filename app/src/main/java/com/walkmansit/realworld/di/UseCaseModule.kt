package com.walkmansit.realworld.di

import com.walkmansit.realworld.domain.repository.ArticleRepository
import com.walkmansit.realworld.domain.repository.AuthRepository
import com.walkmansit.realworld.domain.repository.UserPreferencesRepository
import com.walkmansit.realworld.domain.usecases.CheckAuthUseCase
import com.walkmansit.realworld.domain.usecases.EditArticleUseCase
import com.walkmansit.realworld.domain.usecases.GetArticleUseCase
import com.walkmansit.realworld.domain.usecases.GetArticlesUseCase
import com.walkmansit.realworld.domain.usecases.GetTagsUseCase
import com.walkmansit.realworld.domain.usecases.LoginUseCase
import com.walkmansit.realworld.domain.usecases.LogoutUseCase
import com.walkmansit.realworld.domain.usecases.NewArticleUseCase
import com.walkmansit.realworld.domain.usecases.RegistrationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideCheckAuthUseCase(
        authRepository: AuthRepository,
        userPreferencesRepository: UserPreferencesRepository,
        dispatcherProvider: DefaultDispatcherProvider,
    ) = CheckAuthUseCase(authRepository, userPreferencesRepository, dispatcherProvider)

    @Provides
    fun provideEditArticleUseCase(
        repository: ArticleRepository,
        dispatcherProvider: DefaultDispatcherProvider) = EditArticleUseCase(repository,dispatcherProvider)

    @Provides
    fun provideGetArticlesUseCase(
        articleRepository: ArticleRepository,
        dispatcherProvider: DefaultDispatcherProvider) = GetArticlesUseCase(articleRepository,dispatcherProvider)

    @Provides
    fun provideGetArticleUseCase(
        articleRepository: ArticleRepository,
        dispatcherProvider: DefaultDispatcherProvider) = GetArticleUseCase(articleRepository, dispatcherProvider)

    @Provides
    fun provideGetTagsUseCase(
        articleRepository: ArticleRepository,
        dispatcherProvider: DefaultDispatcherProvider) = GetTagsUseCase(articleRepository, dispatcherProvider)

    @Provides
    fun provideLoginUseCase(
        authRepository: AuthRepository,
        userPreferencesRepository: UserPreferencesRepository,
        dispatcherProvider: DefaultDispatcherProvider
    ) = LoginUseCase(authRepository, userPreferencesRepository, dispatcherProvider)

    @Provides
    fun provideLogoutUseCase(
        userPreferencesRepository: UserPreferencesRepository,
        dispatcherProvider: DefaultDispatcherProvider) =
        LogoutUseCase(userPreferencesRepository, dispatcherProvider)

    @Provides
    fun provideNewArticleUseCase(
        repository: ArticleRepository,
        dispatcherProvider: DefaultDispatcherProvider
    ): NewArticleUseCase = NewArticleUseCase(repository, dispatcherProvider)

    @Provides
    fun provideRegistrationUseCase(
        repository: AuthRepository,
        userPreferencesRepository: UserPreferencesRepository,
        dispatcherProvider: DefaultDispatcherProvider
    ) = RegistrationUseCase(repository, userPreferencesRepository, dispatcherProvider)
}
