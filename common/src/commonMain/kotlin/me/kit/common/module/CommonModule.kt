package me.kit.common.module

import me.kit.commonDomain.network.TruckApisImpl
import me.kit.commonDomain.network.TruckApis
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val commonModule = DI.Module("common module", false) {
    importOnce(platformModule())
    bind<TruckApis>() with singleton { TruckApisImpl(instance()) }
}