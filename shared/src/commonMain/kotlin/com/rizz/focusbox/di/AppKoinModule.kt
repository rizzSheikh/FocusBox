package com.rizz.focusbox.di

import org.koin.dsl.module

val appModule = module {
    includes(AppModule().module())
}
