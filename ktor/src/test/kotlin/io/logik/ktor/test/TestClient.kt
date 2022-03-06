package io.logik.ktor.test

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.*

class TestClient(private val httpClient: HttpClient) {
    
    companion object {

        val apiResponse = IpResponse("127.0.0.1")
        
        fun newInstance(logger: Logger): TestClient {
            val mockEngine = MockEngine {
                respond(
                    content = ByteReadChannel("""{"ip":"${apiResponse.ip}"}"""),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            return TestClient(httpClient(mockEngine, logger))
        }

        private fun httpClient(engine: HttpClientEngine, logger: Logger) = HttpClient(engine) {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
            install(io.logik.ktor.Logging) {
                this.logger = logger
            }
        }
    }

    suspend fun getIp(headers: Map<String, String> = emptyMap()): IpResponse =
        httpClient.get("https://api.ipify.org/?format=json") {
            headers {
                headers.forEach(this::append)
            }
        }

}
