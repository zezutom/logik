package io.logik.core

import io.logik.core.model.TraceId
import io.logik.core.model.TracingContext
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test

class TracingTest {

    @Test
    fun withTraceId() = runBlocking {
        val traceId = TraceId.generate()
        withTraceId(traceId) {
            verifyContext(traceId)
            withContext(currentCoroutineContext()) {
                withTraceId {
                    verifyContext(traceId)
                }
            }
        }
    }
    
    @Test
    fun getTraceId() = runBlocking {
        val traceId = TraceId.generate()
        withTraceId(traceId) {
            assertEquals(traceId, io.logik.core.getTraceId())
        }
    }

    private suspend fun verifyContext(traceId: TraceId) {
        currentCoroutineContext()[TracingContext]?.let {
            assertEquals(traceId, it.traceId)
        } ?: fail("$traceId not found in the context!")
    }
}