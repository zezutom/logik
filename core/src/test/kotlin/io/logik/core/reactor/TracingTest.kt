package io.logik.core.reactor

import io.logik.core.model.TraceId
import io.logik.core.model.TraceId.Companion.TraceIdKey
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono

class TracingTest {

    @Test
    fun withTraceId() {
        val traceId = TraceId.generate()
        withTraceId(traceId) { 
            verifyTraceId(traceId)
                .flatMap { verifyTraceId(traceId) }
        }.block()
    }

    @Test
    fun getTraceId() {
        val traceId = TraceId.generate()
        withTraceId(traceId) {
            io.logik.core.reactor.getTraceId().map {
                assertEquals(traceId, it)
            }.switchIfEmpty { fail("$traceId not found in the context!") }
        }.block()
    }

    private fun verifyTraceId(traceId: TraceId) = Mono.deferContextual {
        assertEquals(traceId, it.getOrDefault(TraceIdKey, null)).toMono()
    }
}