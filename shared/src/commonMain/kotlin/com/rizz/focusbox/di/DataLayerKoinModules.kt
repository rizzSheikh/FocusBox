package com.rizz.focusbox.di

import com.rizz.focusbox.data.di.DaoModule
import com.rizz.focusbox.data.di.RepositoryModule
import com.rizz.focusbox.data.di.module
import org.koin.dsl.module

val daoModule = module {
    includes(DaoModule().module())
}

val repositoryModule = module {
    includes(RepositoryModule().module())
}
