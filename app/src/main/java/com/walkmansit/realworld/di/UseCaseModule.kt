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
    ) = CheckAuthUseCase(authRepository, userPreferencesRepository)

    @Provides
    fun provideEditArticleUseCase(repository: ArticleRepository) = EditArticleUseCase(repository)

    @Provides
    fun provideGetArticlesUseCase(articleRepository: ArticleRepository) = GetArticlesUseCase(articleRepository)

    @Provides
    fun provideGetArticleUseCase(articleRepository: ArticleRepository) = GetArticleUseCase(articleRepository)

    @Provides
    fun provideGetTagsUseCase(articleRepository: ArticleRepository) = GetTagsUseCase(articleRepository)

    @Provides
    fun provideLoginUseCase(
        authRepository: AuthRepository,
        userPreferencesRepository: UserPreferencesRepository,
    ) = LoginUseCase(authRepository, userPreferencesRepository)

    @Provides
    fun provideLogoutUseCase(userPreferencesRepository: UserPreferencesRepository) =
        LogoutUseCase(userPreferencesRepository)

    @Provides
    fun provideNewArticleUseCase(repository: ArticleRepository): NewArticleUseCase = NewArticleUseCase(repository)

    @Provides
    fun provideRegistrationUseCase(
        repository: AuthRepository,
        userPreferencesRepository: UserPreferencesRepository,
    ) = RegistrationUseCase(repository, userPreferencesRepository)
}
