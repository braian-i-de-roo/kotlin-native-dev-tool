package commands.ngrok.util

import com.github.ajalt.clikt.core.CliktError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.curl.Curl
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.appendIfNameAbsent
import io.ktor.utils.io.core.use
import kotlinx.cinterop.ExperimentalForeignApi
import platform.posix.getenv

object NgrokClient {
    @OptIn(ExperimentalForeignApi::class)
    private val ngrokApiKey = getenv("NGROK_TOKEN")

    @OptIn(ExperimentalForeignApi::class)
    private val client =
        HttpClient(Curl) {
            defaultRequest {
                url("https://api.ngrok.com/")
                headers.appendIfNameAbsent("Ngrok-Version", "2")
                headers.appendIfNameAbsent("Authorization", "Bearer $ngrokApiKey")
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 10000
            }
            install(ContentNegotiation) {
                json()
            }
        }

    suspend fun getDomains(): List<Domain> {
        val domainsRequest = "reserved_domains"
        val response =
            client.use {
                val res =
                    runCatching {
                        val aux = client.get(domainsRequest)
                        aux
                    }.onFailure {
                        println(it)
                    }
                res.getOrThrow()
            }
        if (response.status.value == 200) {
            val body = response.body<DomainListResponse>()
            return body.reserved_domains
        } else {
            throw CliktError("Ngrok domain list get request failed with status code ${response.status.value}")
        }
    }
}
