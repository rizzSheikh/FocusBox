package com.rizz.focusbox.data.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan(
    "com.rizz.focusbox.data.timer",
    "com.rizz.focusbox.data.stats",
    "com.rizz.focusbox.data.settings",
    "com.rizz.focusbox.data.onboarding",
    "com.rizz.focusbox.data.theme",
)
class RepositoryModule
