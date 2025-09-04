package com.walkmansit.realworld.domain.util

fun interface ModelsMapper<R, T> {
    fun map(data: R): T
}