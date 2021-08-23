package com.jaydev.github.remote.mapper

import com.jaydev.github.domain.entity.User
import com.jaydev.github.remote.model.UserModel


class UserEntityMapper : EntityMapper<UserModel, User> {
    override fun mapFromRemote(model: UserModel) = User(model.login, model.avatar_url)
}
