package com.jaydev.github.domain.interactor.usecase

import com.jaydev.github.domain.NetResult
import com.jaydev.github.domain.entity.Fork
import com.jaydev.github.domain.entity.Repo
import com.jaydev.github.domain.entity.User
import com.jaydev.github.domain.interactor.ErrorHandler
import com.jaydev.github.domain.repository.GithubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip

class GetUserDataUseCase(
    private val githubRepository: GithubRepository,
    private val errorHandler: ErrorHandler
) : BaseUseCase<Flow<NetResult<Pair<User, List<Repo>>>>, GetUserDataUseCase.Params>() {
    override suspend fun run(params: Params) = githubRepository.getUser(params.userName)
        .zip(githubRepository.getRepositories(params.userName)) { user, repositories ->
            Pair(user, repositories.sortedByDescending { it.starCount })
        }.toResult(errorHandler)

    data class Params(
        val userName: String
    )
}

class GetRepoDetailUseCase(
    private val githubRepository: GithubRepository,
    private val errorHandler: ErrorHandler
) : BaseUseCase<Flow<NetResult<Pair<Repo, List<Fork>>>>, GetRepoDetailUseCase.Params>() {
    override suspend fun run(params: Params) =
        githubRepository.getRepository(params.userName, params.id)
            .zip(githubRepository.getForks(params.userName, params.id)) { repository, forks ->
                Pair(repository, forks)
            }.toResult(errorHandler)

    data class Params(
        val userName: String,
        val id: String
    )
}
