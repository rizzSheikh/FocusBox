package com.rizz.focusbox

import android.app.Application
import com.rizz.focusbox.di.initKoin
import org.koin.android.ext.koin.androidContext

class FocusBoxApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@FocusBoxApplication)
        }
    }
}
