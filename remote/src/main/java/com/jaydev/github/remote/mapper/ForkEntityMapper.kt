package com.jaydev.github.remote.mapper

import com.jaydev.github.domain.entity.Fork
import com.jaydev.github.remote.model.ForkModel

class ForkEntityMapper(
    private val userMapper: UserEntityMapper
) : EntityMapper<ForkModel, Fork> {
    override fun mapFromRemote(model: ForkModel) = Fork(
        model.name,
        model.full_name,
        userMapper.mapFromRemote(model.owner)
    )
}
