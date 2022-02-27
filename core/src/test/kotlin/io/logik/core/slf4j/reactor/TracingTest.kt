package io.logik.core.slf4j.reactor

import ch.qos.logback.classic.Level
import io.logik.core.model.TraceId
import io.logik.core.slf4j.MemoryAppender
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.kotlin.core.publisher.toMono

class TracingTest {

    private val logger: Logger = LoggerFactory.getLogger(TracingTest::class.java)

    private lateinit var memoryAppender: MemoryAppender

    @BeforeEach
    fun before() {
        memoryAppender = MemoryAppender.logbackInstance(logger)
    }

    @AfterEach
    fun after() {
        memoryAppender.reset()
    }

    @Test
    fun withTraceId() {
        val traceId = TraceId.generate()
        withTraceId(traceId) {
            logger.info("Test").toMono()
        }.block()
        assertTrue(memoryAppender.containsTraceId(traceId, Level.INFO))
    }
}