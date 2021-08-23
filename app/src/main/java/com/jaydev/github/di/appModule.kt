package com.jaydev.github.di

import android.content.ContentResolver
import com.jaydev.github.di.loader.ResourcesProvider
import com.jaydev.github.di.loader.ResourcesProviderImpl
import com.jaydev.github.domain.interactor.ErrorHandler
import com.jaydev.github.domain.interactor.usecase.GetRepoDetailUseCase
import com.jaydev.github.domain.interactor.usecase.GetUserDataUseCase
import com.jaydev.github.domain.repository.GithubRepository
import com.jaydev.github.model.interactor.GithubDataRepository
import com.jaydev.github.model.interactor.NetErrorHandlerImpl
import com.jaydev.github.model.source.GithubRemote
import com.jaydev.github.remote.GithubRemoteImpl
import com.jaydev.github.remote.GithubServiceFactory
import com.jaydev.github.remote.mapper.ForkEntityMapper
import com.jaydev.github.remote.mapper.RepoEntityMapper
import com.jaydev.github.remote.mapper.UserEntityMapper
import com.jaydev.github.ui.main.MainViewModel
import com.jaydev.github.ui.repo.RepoDetailViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module = module {
    //presentation
    single<ContentResolver> { androidContext().contentResolver }
    factory { ResourcesProviderImpl(androidApplication()) as ResourcesProvider }
    viewModel { (userName: String) -> MainViewModel(userName, get()) }
    viewModel { (userName: String, repoName: String) -> RepoDetailViewModel(userName, repoName, get()) }

    //domain
    factory { GetUserDataUseCase(get(), get()) }
    factory { GetRepoDetailUseCase(get(), get()) }

    //data
    single { GithubDataRepository(get()) as GithubRepository }
    single { NetErrorHandlerImpl(get()) as ErrorHandler }

    //remote
    single { GithubRemoteImpl(get(), get(), get(), get()) as GithubRemote }

    single { RepoEntityMapper() }
    single { UserEntityMapper() }
    single { ForkEntityMapper(get()) }

    single { GithubServiceFactory.makeOkHttpClient() }
    single {
        GithubServiceFactory.makeRetrofit(
            "https://api.github.com",
            get()
        )
    }
    single { GithubServiceFactory.makeGithubService(get()) }
}