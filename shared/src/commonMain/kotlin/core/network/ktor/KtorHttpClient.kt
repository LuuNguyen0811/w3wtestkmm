package core.network.ktor

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorHttpClient {
    private val API_KEY =
        "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NGIwMTkyNmZhYTg2ODRlMmJiY2ZkZTI4ZTgwNTZkOSIsIm5iZiI6MTc0OTcxNjc2OC4xNDksInN1YiI6IjY4NGE4ZjIwNzBlODY4Yjg4MGIwZDNiNCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.7DDv7c1TnQJn7KI9gAbBK-8R37JOBt7eQmB9-nqlePM"
    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        defaultRequest {
            headers.append("Authorization", "Bearer ${API_KEY}")
        }
    }
}