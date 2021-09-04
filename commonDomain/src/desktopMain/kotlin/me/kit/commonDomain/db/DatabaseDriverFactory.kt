package me.kit.commonDomain.db

import com.me.kit.TruckDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

actual class DatabaseDriverFactory {
  actual fun createDriver(): SqlDriver {
    val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    TruckDatabase.Schema.create(driver)
    return driver
  }
}