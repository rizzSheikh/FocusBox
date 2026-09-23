package com.rizz.focusbox.feature.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.rizz.focusbox.feature.onboarding.view.OnboardingPagerScreenMain
import kotlinx.serialization.Serializable

@Serializable
data object OnboardingPager

fun NavController.navigateToOnboardingScreen(navOptions: NavOptions? = null) {
    navigate(route = OnboardingPager, navOptions = navOptions)
}

fun NavGraphBuilder.onBoardingScreen(
    onNavigateToNotificationRationale: () -> Unit,
    onFinished: () -> Unit,
) {
    composable<OnboardingPager> {
        OnboardingPagerScreenMain(
            onNavigateToNotificationRationale = onNavigateToNotificationRationale,
            onFinished = onFinished,
        )
    }
}
