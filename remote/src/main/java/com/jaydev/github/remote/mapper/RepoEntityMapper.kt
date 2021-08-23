package com.jaydev.github.remote.mapper

import com.jaydev.github.domain.entity.Repo
import com.jaydev.github.remote.model.RepoModel

class RepoEntityMapper : EntityMapper<RepoModel, Repo> {
    override fun mapFromRemote(model: RepoModel) = Repo(
        model.name,
        model.description ?: "",
        model.stargazers_count
    )
}
