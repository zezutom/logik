package io.logik.ktor.test

import io.ktor.client.features.logging.*
import io.logik.ktor.LoggingTest
import org.slf4j.LoggerFactory

class TestLogger : Logger {
    val delegate: ch.qos.logback.classic.Logger =
        LoggerFactory.getLogger(LoggingTest::class.java) as ch.qos.logback.classic.Logger

    override fun log(message: String) {
        delegate.info(message)
    }
}