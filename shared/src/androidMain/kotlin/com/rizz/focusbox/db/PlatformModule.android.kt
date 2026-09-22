package com.rizz.focusbox.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.koin.dsl.module

actual val platformModule = module {
    single<SqlDriver> { AndroidSqliteDriver(FocusBoxDatabase.Schema, get(), "focusbox.db") }
}
