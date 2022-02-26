package io.logik.core

import io.logik.core.model.TraceId
import io.logik.core.model.TracingContext
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TracingTest {
    
    @Test
    fun withTraceId() = runBlocking { 
        val traceId = TraceId.generate()
        withTraceId(traceId) {
            currentCoroutineContext()[TracingContext]?.let { 
                assertEquals(traceId, it.traceId)
            } ?: fail("$traceId not found in the context!")
        }
    }
    
    @Test
    fun getTraceId() = runBlocking {
        val traceId = TraceId.generate()
        withTraceId(traceId) {
            assertEquals(traceId, io.logik.core.getTraceId())
        }
    }
}