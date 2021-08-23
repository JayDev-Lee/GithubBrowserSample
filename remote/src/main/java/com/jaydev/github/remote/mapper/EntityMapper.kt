package com.jaydev.github.remote.mapper

interface EntityMapper<in MODEL, out ENTITY> {
    fun mapFromRemote(model: MODEL): ENTITY
}
