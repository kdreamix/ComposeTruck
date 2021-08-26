package me.kit.common

import io.ktor.client.*
import io.ktor.client.engine.curl.*
import io.ktor.client.features.logging.*

val client = HttpClient(Curl) {
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.HEADERS
    }
    engine {
        // this: CurlClientEngineConfig
        sslVerify = true
    }
}