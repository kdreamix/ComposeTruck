package me.kit.commonDomain.db

import com.me.kit.TruckDatabase

class SqlDriverProvider {
    private val databaseDriverFactory: DatabaseDriverFactory = DatabaseDriverFactory()
    val sqldriver = databaseDriverFactory.createDriver()
}