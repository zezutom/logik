package io.logik.core

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.reactor.ReactorContext
import kotlinx.coroutines.reactor.asCoroutineContext
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import reactor.util.context.Context

class ContextTest {

    @Test
    fun withReactorContext() = runBlocking {
        val context = Context.of("answer", 42)
        withReactorContext(context) {
            assertEquals(context, currentCoroutineContext()[ReactorContext]?.context)
        }
    }

    @Test
    fun withCoroutineContext() {
        runBlocking {
            val context = Context.of("answer", 42)
            withReactorContext(context) {
                withCoroutineContext {
                    Mono.deferContextual {
                        assertEquals(context, it.asCoroutineContext().context).toMono()
                    }
                }
            }
        }
    }
}