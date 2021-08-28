package me.kit.common.module

import io.ktor.client.*
import me.kit.common.client
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

actual fun platformModule() = DI.Module("app Module", false){
    bind<HttpClient>() with singleton { client }
}