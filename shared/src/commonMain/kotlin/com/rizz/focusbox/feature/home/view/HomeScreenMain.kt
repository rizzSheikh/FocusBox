package com.rizz.focusbox.feature.home.view

import androidx.compose.runtime.Composable
import com.rizz.focusbox.feature.home.view.components.HomeScreen
import com.rizz.focusbox.feature.home.viewmodel.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreenMain(viewModel: HomeViewModel = koinViewModel()) {
    HomeScreen()
}
