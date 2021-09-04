package me.kit.commonDomain.db

import android.content.Context
import com.me.kit.TruckDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory(private val context: Context) {
  actual fun createDriver(): SqlDriver {
    return AndroidSqliteDriver(TruckDatabase.Schema, context, "truckroute.db")
  }
}
