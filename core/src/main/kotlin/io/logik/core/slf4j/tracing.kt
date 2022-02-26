package io.logik.core.slf4j

import io.logik.core.model.TraceId
import io.logik.core.model.TraceId.Companion.TraceIdKey
import io.logik.core.model.TracingContext
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext
import org.slf4j.MDC

suspend fun <T> withTraceId(traceId: TraceId, f: () -> T): T =
    MDC.putCloseable(TraceIdKey, traceId.value).use {
        withContext(MDCContext().plus(TracingContext(traceId))) { f() }
    }

//fun <T> withTraceId(traceId: TraceId, f: () -> Mono<T>): Mono<T> =
//    MDC.putCloseable(TraceIdKey, traceId.value).use {
//        io.logik.core.withTraceId(traceId) {
//            f().contextWrite { it.put(TraceIdKey, traceId) }
//        }
//    }
