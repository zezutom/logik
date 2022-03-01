package io.logik.core

import io.logik.core.model.TraceId
import io.logik.core.model.TracingContext
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext

suspend fun <T> withTraceId(traceId: TraceId, f: suspend () -> T): T =
    withContext(currentCoroutineContext().plus(TracingContext(traceId))) { f() }

suspend fun <T> withTraceId(f: suspend () -> T): T =
    currentCoroutineContext()[TracingContext]?.let { withTraceId(it.traceId, f) } ?: run { f() }

suspend fun getTraceId(): TraceId? = currentCoroutineContext()[TracingContext]?.traceId




