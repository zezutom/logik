package io.logik.ktor

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import io.logik.core.slf4j.MemoryAppender
import io.logik.ktor.test.TestClient
import io.logik.ktor.test.TestLogger
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class LoggingTest {

    private lateinit var client: TestClient

    private lateinit var memoryAppender: MemoryAppender

    @BeforeEach
    fun before() {
        val logger = TestLogger()
        client = TestClient.newInstance(logger)
        memoryAppender = memoryAppender(logger)
        memoryAppender.start()
    }

    @AfterEach
    fun after() {
        memoryAppender.reset()
        memoryAppender.stop()
    }

    @Test
    fun hello() = runBlocking {
        client.getIp()
        assertTrue(
            memoryAppender.contains(
                "GET https://api.ipify.org/?format=json, headers: [Accept=[application/json], Accept-Charset=[UTF-8]]",
                Level.INFO
            ), "Request should be logged!"
        )
    }

    private fun memoryAppender(logger: TestLogger): MemoryAppender {
        val memoryAppender = MemoryAppender()
        memoryAppender.context = LoggerFactory.getILoggerFactory() as LoggerContext
        val logbackLogger = logger.delegate
        logbackLogger.level = Level.INFO
        logbackLogger.addAppender(memoryAppender)
        return memoryAppender
    }
}
