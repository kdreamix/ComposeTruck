package me.kit.common

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*

val androidHttpClient = HttpClient(OkHttp) {
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }
    install(JsonFeature){
        serializer = KotlinxSerializer()
    }

    defaultRequest {
        header("Content-Type", "application/json;charset=UTF-8")
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