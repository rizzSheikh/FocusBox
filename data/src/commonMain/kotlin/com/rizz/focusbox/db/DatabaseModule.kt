package com.rizz.focusbox.db

import org.koin.dsl.module

val databaseModule = module {
    single { FocusBoxDatabase(get()) }
}
