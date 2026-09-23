package com.rizz.focusbox.feature.splash.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.rizz.focusbox.feature.splash.view.components.SplashScreen
import com.rizz.focusbox.feature.splash.view.effect.SplashScreenEffect
import com.rizz.focusbox.feature.splash.viewmodel.SplashViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreenMain(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SplashViewModel = koinViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                SplashScreenEffect.NavigateToOnboarding -> onNavigateToOnboarding()
                SplashScreenEffect.NavigateToHome -> onNavigateToHome()
            }
        }
    }

    SplashScreen(onEvent = viewModel::onEvent)
}
