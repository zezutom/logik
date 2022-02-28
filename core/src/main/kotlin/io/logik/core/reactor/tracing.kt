package io.logik.core.reactor

import io.logik.core.model.TraceId
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

fun <T> withTraceId(traceId: TraceId, f: () -> Mono<T>): Mono<T> =
    f().contextWrite { context ->
        context.put(TraceId.TraceIdKey, traceId)
    }

fun getTraceId(): Mono<TraceId?> = Mono.deferContextual { cv ->
    cv.getOrDefault<TraceId>(TraceId.TraceIdKey, null).toMono()
}