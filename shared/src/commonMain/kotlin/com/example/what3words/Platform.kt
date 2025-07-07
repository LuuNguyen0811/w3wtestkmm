package com.example.what3words

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform