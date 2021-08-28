package me.kit.common

import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*

val client = HttpClient(Java) {
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.HEADERS
    }
    install(JsonFeature){
        serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
            prettyPrint = true
            isLenient = true
        })
    }

    defaultRequest {
        header("Content-Type", "application/json;charset=UTF-8")
    }
}