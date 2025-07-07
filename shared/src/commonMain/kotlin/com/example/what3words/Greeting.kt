package com.example.what3words
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }

    suspend fun testKtor(): Unit {
        val client = HttpClient()
        val response = client.get("https://ktor.io/docs/")
        println(response.bodyAsText())
    }
}