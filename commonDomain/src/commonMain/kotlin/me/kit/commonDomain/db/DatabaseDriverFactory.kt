package me.kit.commonDomain.db

import com.squareup.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory constructor() {
    fun createDriver(): SqlDriver
}
