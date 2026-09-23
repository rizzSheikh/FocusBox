package com.rizz.focusbox.feature.splash.view.effect

sealed class SplashScreenEffect {
    data object NavigateToOnboarding : SplashScreenEffect()
    data object NavigateToHome : SplashScreenEffect()
}
