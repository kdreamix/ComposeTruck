package me.kit.common

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*

val androidHttpClient = HttpClient(OkHttp) {
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