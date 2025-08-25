package com.walkmansit.realworld.domain.util

interface ModelsMapper<R, T> {
    fun map(data: R): T
}