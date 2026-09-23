package com.rizz.focusbox.feature.onboarding.view.effect

sealed class OnBoardingScreenEffect {
    data object NavigateToNotificationRationale : OnBoardingScreenEffect()
    data object NavigateHome : OnBoardingScreenEffect()
}
