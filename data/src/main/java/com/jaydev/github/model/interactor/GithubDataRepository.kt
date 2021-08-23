package com.jaydev.github.model.interactor

import com.jaydev.github.model.source.GithubRemote
import com.jaydev.github.domain.repository.GithubRepository

class GithubDataRepository(
    private val remote: GithubRemote
) : GithubRepository {
    override suspend fun getForks(userName: String, id: String) = remote.getForks(
        userName = userName,
        id = id
    )

    override suspend fun getRepositories(userName: String) = remote.getRepositories(userName)

    override suspend fun getRepository(userName: String, id: String) = remote.getRepository(
        userName = userName,
        id = id
    )

    override suspend fun getUser(userName: String) = remote.getUser(userName)
}