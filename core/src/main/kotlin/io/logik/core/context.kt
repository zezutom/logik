package io.logik.core

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.reactor.ReactorContext
import kotlinx.coroutines.reactor.asCoroutineContext
import kotlinx.coroutines.withContext
import reactor.core.publisher.Mono
import reactor.util.context.Context

suspend fun <T> withReactorContext(context: Context, f: suspend () -> T): T =
    withContext(context.asCoroutineContext()) { f() }

suspend fun <T> withCoroutineContext(f: () -> Mono<T>): Mono<T> =
    currentCoroutineContext()[ReactorContext]?.let { reactorContext ->
        f().contextWrite {
            it.putAll(reactorContext.context.readOnly())
        }
    } ?: f()