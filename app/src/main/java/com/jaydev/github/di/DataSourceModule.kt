package com.jaydev.github.di

import com.jaydev.github.model.source.GithubRemote
import com.jaydev.github.remote.GithubRemoteImpl
import com.jaydev.github.remote.GithubService
import com.jaydev.github.remote.mapper.ForkEntityMapper
import com.jaydev.github.remote.mapper.RepoEntityMapper
import com.jaydev.github.remote.mapper.UserEntityMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataSourceModule {
    @Provides
    @Singleton
    fun provideGithubRemote(
        githubService: GithubService,
        userEntityMapper: UserEntityMapper,
        repoEntityMapper: RepoEntityMapper,
        forkEntityMapper: ForkEntityMapper
    ): GithubRemote {
        return GithubRemoteImpl(
            githubService,
            userEntityMapper,
            repoEntityMapper,
            forkEntityMapper
        )
    }

    @Provides
    @Singleton
    fun provideRepoEntityMapper() = RepoEntityMapper()

    @Provides
    @Singleton
    fun provideUserEntityMapper() = UserEntityMapper()

    @Provides
    @Singleton
    fun provideForkEntityMapper(userEntityMapper: UserEntityMapper) =
        ForkEntityMapper(userEntityMapper)
}