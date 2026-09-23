package com.rizz.focusbox.feature.onboarding.view.event

sealed class OnBoardingScreenEvents {
    data object OnSkipClick : OnBoardingScreenEvents()
    data object OnNextClick : OnBoardingScreenEvents()
}
