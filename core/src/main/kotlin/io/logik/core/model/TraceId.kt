package io.logik.core.model

import java.util.concurrent.ThreadLocalRandom

@JvmInline
value class TraceId(val value: String) {
    companion object {
        const val TraceIdKey = "traceId"
        
        private val AllowedChars = ('a'..'z')
            .plus(('A'..'Z'))
            .plus(('0'..'9'))
        
        fun generate(length: Int = 10): TraceId {
            val rnd = ThreadLocalRandom.current()
            val value = String(
                (0 until length)
                    .map { AllowedChars[rnd.nextInt(AllowedChars.size)] }
                    .toCharArray())
            return TraceId(value)
        }
    }
}