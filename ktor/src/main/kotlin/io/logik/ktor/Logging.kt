package io.logik.ktor

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.util.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import io.logik.core.withTraceId
import java.nio.charset.Charset

class Logging(val logger: Logger, private val level: LogLevel) {
    
    class Config {
        var logger = Logger.DEFAULT
        var level = LogLevel.HEADERS
    }

    companion object : HttpClientFeature<Config, Logging> {
        override val key: AttributeKey<Logging> =
            AttributeKey(Logging::class.simpleName!!)

        override fun install(feature: Logging, scope: HttpClient) {
            scope.sendPipeline.intercept(HttpSendPipeline.Monitoring) {
                withTraceId {
                    feature.logRequest(context)
                }
            }
            scope.receivePipeline.intercept(HttpReceivePipeline.After) {
                val (loggingContent, responseContent) = it.content.split(scope)
                val newClientCall = context.wrapWithContent(responseContent)
                val sideCall = context.wrapWithContent(loggingContent)
                withTraceId { 
                    feature.logResponse(sideCall.response)
                }
                proceedWith(newClientCall.response)
            }
        }

        override fun prepare(block: Config.() -> Unit): Logging {
            val config = Config().apply(block)
            return Logging(config.logger, config.level)
        }
    }

    private fun logRequest(request: HttpRequestBuilder) {
        val sb = StringBuilder("Request")
        if (level.info) {
            sb.append(" ${request.method.value} ${Url(request.url)}, ")
        }
        if (level.headers) {
            sb.append("headers: ${request.headers.entries().sortedBy { it.key }}, ")
        }
        if (level.body) {
            val body = when (val body = request.body) {
                is TextContent -> String(body.bytes())
                else -> "[request body omitted]"
            }
            sb.append("body: $body")
        }
        logger.log(sb.toString().trim().trimEnd(','))
    }
    
    private suspend fun logResponse(response: HttpResponse) {
        val sb = StringBuilder("Response")
        if (level.info) {
            val from = "${response.call.request.method.value} ${response.call.request.url}"
            val statusCode = "${response.status.value} ${response.status.description}"
            sb.append(" from: $from, status code: $statusCode, ")
        }
        if (level.headers) {
            sb.append("headers: ${response.headers.entries().sortedBy { it.key }}, ")
        }
        if (level.body) {
            val body = response.contentType()?.let { contentType ->
                val charset = contentType.charset() ?: Charsets.UTF_8
                response.content.tryReadText(charset) ?: "[response body omitted]"
            }
            sb.append("body: $body")
        }
        logger.log(sb.toString().trim().trimEnd(','))
    }

    private suspend inline fun ByteReadChannel.tryReadText(charset: Charset): String? = try {
        readRemaining().readText(charset = charset)
    } catch (cause: Throwable) {
        null
    }
}