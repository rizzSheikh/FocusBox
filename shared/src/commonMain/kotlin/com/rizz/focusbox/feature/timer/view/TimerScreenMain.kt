package com.rizz.focusbox.feature.timer.view

import androidx.compose.runtime.Composable
import com.rizz.focusbox.feature.timer.view.components.TimerScreen
import com.rizz.focusbox.feature.timer.viewmodel.TimerViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TimerScreenMain(viewModel: TimerViewModel = koinViewModel()) {
    TimerScreen()
}
