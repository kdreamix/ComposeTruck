package me.kit.common

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.logging.*

val androidHttpClient = HttpClient(OkHttp) {
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.HEADERS
    }
    engine {
        // this: OkHttpConfig

        config {
            // this: OkHttpClient.Builder
            followRedirects(true)
            // ...
        }

        //addInterceptor(interceptor)
        //addNetworkInterceptor(interceptor)

        //preconfigured = okHttpClientInstance
    }
}