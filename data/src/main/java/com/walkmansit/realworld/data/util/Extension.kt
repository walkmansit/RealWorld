package com.walkmansit.realworld.data.util

import com.walkmansit.realworld.domain.model.RequestFailed
import com.walkmansit.realworld.domain.util.Either
import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <T> safeApiCall(
    crossinline block: suspend () -> T,
    crossinline errorMapper: (Throwable) -> RequestFailed = { e ->
        RequestFailed(commonError = e.message.orEmpty())
    }
): Either<RequestFailed, T> = runCatching {
    block()
}.onFailure { e ->
    if (e is CancellationException) throw e
}.fold(
    onSuccess = { Either.success(it) },
    onFailure = { e -> Either.fail(errorMapper(e)) }
)