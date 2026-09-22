package com.rizz.focusbox.di

import com.rizz.focusbox.db.databaseModule
import com.rizz.focusbox.db.platformModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) = startKoin {
    config?.invoke(this)
    modules(AppModule().module(), platformModule, databaseModule)
}
