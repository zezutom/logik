package io.logik.core.slf4j.reactor

import io.logik.core.model.TraceId
import io.logik.core.model.TraceId.Companion.TraceIdKey
import org.slf4j.MDC
import reactor.core.publisher.Mono

fun <T> withTraceId(traceId: TraceId, f: () -> Mono<T>): Mono<T> =
    MDC.putCloseable(TraceIdKey, traceId.value).use {
        io.logik.core.reactor.withTraceId(traceId) {
            f().contextWrite { it.put(TraceIdKey, traceId) }
        }
    }
