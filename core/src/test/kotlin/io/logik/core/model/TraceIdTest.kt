package io.logik.core.model

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TraceIdTest {

    @Test
    fun generate() {
        assertTrue(TraceId.generate().value.matches(Regex("[a-zA-Z0-9]{10}")))
    }

    @Test
    fun generateDefinedLength() {
        assertTrue(TraceId.generate(length = 15).value.matches(Regex("[a-zA-Z0-9]{15}")))
    }
}