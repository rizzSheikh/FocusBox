package com.rizz.focusbox.feature.stats.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.rizz.focusbox.feature.stats.view.components.StatsScreen
import com.rizz.focusbox.feature.stats.viewmodel.StatsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StatsScreenMain(viewModel: StatsViewModel = koinViewModel()) {
    StatsScreen()
}
