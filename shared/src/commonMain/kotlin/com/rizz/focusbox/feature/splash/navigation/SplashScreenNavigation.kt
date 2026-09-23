package com.rizz.focusbox.feature.splash.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rizz.focusbox.feature.splash.view.SplashScreenMain
import kotlinx.serialization.Serializable

@Serializable
data object Splash

fun NavGraphBuilder.splashScreen(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit,
) {
    composable<Splash> {
        SplashScreenMain(
            onNavigateToOnboarding = onNavigateToOnboarding,
            onNavigateToHome = onNavigateToHome,
        )
    }
}
