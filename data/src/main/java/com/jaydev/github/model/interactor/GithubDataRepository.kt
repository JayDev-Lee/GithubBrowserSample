package com.jaydev.github.model.interactor

import com.jaydev.github.domain.repository.GithubRepository
import com.jaydev.github.model.source.GithubRemote

class GithubDataRepository(
    private val remote: GithubRemote
) : GithubRepository {
    override fun getForks(userName: String, id: String) = remote.getForks(
        userName = userName,
        id = id
    )

    override fun getRepositories(userName: String) = remote.getRepositories(userName)


    override fun getRepository(userName: String, id: String) = remote.getRepository(
        userName = userName,
        id = id
    )

    override fun getUser(userName: String) = remote.getUser(userName)
}