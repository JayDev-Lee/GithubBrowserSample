package com.jaydev.github.domain.repository

import com.jaydev.github.domain.entity.Fork
import com.jaydev.github.domain.entity.Repo
import com.jaydev.github.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface GithubRepository {
    fun getForks(userName: String, id: String): Flow<List<Fork>>
    fun getRepositories(userName: String): Flow<List<Repo>>
    fun getRepository(userName: String, id: String): Flow<Repo>
    fun getUser(userName: String): Flow<User>
}