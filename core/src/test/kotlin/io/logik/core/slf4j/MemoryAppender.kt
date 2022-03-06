package io.logik.core.slf4j

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import io.logik.core.model.TraceId
import io.logik.core.model.TraceId.Companion.TraceIdKey
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * In-memory logging.
 * Credit: https://www.baeldung.com/junit-asserting-logs
 */
class MemoryAppender : ListAppender<ILoggingEvent>() {

    companion object {
        fun logbackInstance(logger: Logger): MemoryAppender {
            val memoryAppender = MemoryAppender()
            memoryAppender.context = LoggerFactory.getILoggerFactory() as LoggerContext
            val logbackLogger = logger as ch.qos.logback.classic.Logger
            logbackLogger.level = Level.TRACE
            logbackLogger.addAppender(memoryAppender)
            memoryAppender.start()
            return memoryAppender
        }
    }

    fun reset() {
        this.list.clear()
        this.stop()
    }

    fun containsTraceId(traceId: TraceId, level: Level): Boolean =
        this.list.any { it.mdcPropertyMap[TraceIdKey] == traceId.value && it.level == level }

    fun contains(message: String, level: Level): Boolean =
        this.list.stream()
            .anyMatch { event -> event.toString().contains(message) && event.level == level }
}