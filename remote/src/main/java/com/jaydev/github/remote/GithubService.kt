package com.jaydev.github.remote

import com.jaydev.github.remote.model.ForkModel
import com.jaydev.github.remote.model.RepoModel
import com.jaydev.github.remote.model.UserModel
import retrofit2.http.*

interface GithubService {
    @GET("/users/{userName}")
    suspend fun getUser(
        @Path("userName") userName: String
    ): UserModel

    @GET("/users/{userName}/repos")
    suspend fun getRepositories(
        @Path("userName") userName: String
    ): List<RepoModel>

    @GET("/repos/{userName}/{repo}")
    suspend fun getRepository(
        @Path("userName") userName: String,
        @Path("repo") repoName: String
    ): RepoModel

    @GET("/repos/{userName}/{repo}/forks")
    suspend fun getForks(
        @Path("userName") userName: String,
        @Path("repo") repoName: String
    ): List<ForkModel>
}