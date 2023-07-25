package com.jaydev.github.di

import com.jaydev.github.domain.interactor.ErrorHandler
import com.jaydev.github.domain.repository.GithubRepository
import com.jaydev.github.model.interactor.GithubDataRepository
import com.jaydev.github.model.interactor.NetErrorHandlerImpl
import com.jaydev.github.model.source.GithubRemote
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DomainModule {

    @Provides
    @Singleton
    fun provideGithubRepository(githubRemote: GithubRemote): GithubRepository {
        return GithubDataRepository(githubRemote)
    }

    @Provides
    @Singleton
    fun provideErrorHandler(retrofit: Retrofit): ErrorHandler {
        return NetErrorHandlerImpl(retrofit)
    }
}