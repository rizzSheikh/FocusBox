package com.rizz.focusbox

import androidx.compose.ui.window.ComposeUIViewController
import com.rizz.focusbox.di.initKoin

private var koinStarted = false

fun MainViewController() = ComposeUIViewController {
    if (!koinStarted) {
        initKoin()
        koinStarted = true
    }
    App()
}