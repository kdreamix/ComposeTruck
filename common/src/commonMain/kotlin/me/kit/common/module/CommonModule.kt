package me.kit.common.module

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.me.kit.TruckDatabase
import com.squareup.sqldelight.db.SqlDriver
import me.kit.common.bloc.RootComponent
import me.kit.commonDomain.db.SqlDriverProvider
import me.kit.commonDomain.network.TruckApisImpl
import me.kit.commonDomain.network.TruckApis
import me.kit.commonDomain.repo.TruckRepo
import me.kit.commonDomain.repo.TruckRepoImpl
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val commonModule = DI.Module("common module", false) {
    importOnce(platformModule())
    bind<TruckDatabase>() with singleton { TruckDatabase(instance()) }
    bind<SqlDriver>() with singleton { SqlDriverProvider().sqldriver }
    bind<TruckApis>() with singleton { TruckApisImpl(instance()) }
    bind<TruckRepo>() with singleton { TruckRepoImpl(instance(), instance()) }
    bind<RootComponent>() with singleton { RootComponent(DefaultComponentContext(LifecycleRegistry()), instance()) }
}