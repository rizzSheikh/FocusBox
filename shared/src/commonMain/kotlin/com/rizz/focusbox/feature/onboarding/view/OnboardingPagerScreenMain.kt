package com.rizz.focusbox.feature.onboarding.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.rizz.focusbox.feature.onboarding.view.components.OnboardingPagerScreen
import com.rizz.focusbox.feature.onboarding.view.effect.OnBoardingScreenEffect
import com.rizz.focusbox.feature.onboarding.viewmodel.OnboardingViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingPagerScreenMain(
    onNavigateToNotificationRationale: () -> Unit,
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                OnBoardingScreenEffect.NavigateToNotificationRationale -> onNavigateToNotificationRationale()
                OnBoardingScreenEffect.NavigateHome -> onFinished()
            }
        }
    }

    OnboardingPagerScreen(onEvent = viewModel::onEvent)
}
