package com.jaydev.github.model.source

import com.jaydev.github.domain.entity.Fork
import com.jaydev.github.domain.entity.Repo
import com.jaydev.github.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface GithubRemote {
    suspend fun getForks(userName: String, id: String): Flow<List<Fork>>
    suspend fun getRepositories(userName: String): Flow<List<Repo>>
    suspend fun getRepository(userName: String, id: String): Flow<Repo>
    suspend fun getUser(userName: String): Flow<User>
}