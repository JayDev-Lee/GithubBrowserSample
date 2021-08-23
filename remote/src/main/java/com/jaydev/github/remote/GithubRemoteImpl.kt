package com.jaydev.github.remote

import com.jaydev.github.model.source.GithubRemote
import com.jaydev.github.remote.mapper.ForkEntityMapper
import com.jaydev.github.remote.mapper.RepoEntityMapper
import com.jaydev.github.remote.mapper.UserEntityMapper
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GithubRemoteImpl(
    private val githubService: GithubService,
    private val userEntityMapper: UserEntityMapper,
    private val repoEntityMapper: RepoEntityMapper,
    private val forkEntityMapper: ForkEntityMapper
) : GithubRemote {
    override suspend fun getForks(userName: String, id: String) = flow {
        emit(
            githubService.getForks(
                userName = userName,
                repoName = id
            )
        )
    }.map { forks ->
        forks.map {
            forkEntityMapper.mapFromRemote(it)
        }
    }

    override suspend fun getRepositories(userName: String) = flow {
        emit(
            githubService.getRepositories(userName)
        )
    }.map { repositories ->
        repositories.map {
            repoEntityMapper.mapFromRemote(it)
        }
    }

    override suspend fun getRepository(userName: String, id: String) = flow {
        emit(
            githubService.getRepository(
                userName = userName,
                repoName = id
            )
        )
    }.map {
        repoEntityMapper.mapFromRemote(it)
    }

    override suspend fun getUser(userName: String) = flow {
        emit(
            githubService.getUser(userName)
        )
    }.map {
        userEntityMapper.mapFromRemote(it)
    }
}