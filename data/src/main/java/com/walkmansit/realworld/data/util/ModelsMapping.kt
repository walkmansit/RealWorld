package com.walkmansit.realworld.data.util

import com.google.gson.Gson
import com.walkmansit.realworld.data.model.response.BaseErrorResponse
import com.walkmansit.realworld.domain.model.CommonError
import com.walkmansit.realworld.domain.util.Either
import com.walkmansit.realworld.domain.util.ModelsMapper

inline fun <reified T> getErrorResponse(body: String): T =
    Gson().fromJson(
        body,
        T::class.java,
    )

inline fun <reified R, T> getErrorEither(
    body: String,
    mapper: ModelsMapper<R, T>,
): Either<CommonError, T> =
    try {
        val result = getErrorResponse<BaseErrorResponse>(body)
        Either.fail(CommonError(result.error))
    } catch (e: Exception) {
        val result = getErrorResponse<R>(body)
        Either.success(mapper.map(result))
    }