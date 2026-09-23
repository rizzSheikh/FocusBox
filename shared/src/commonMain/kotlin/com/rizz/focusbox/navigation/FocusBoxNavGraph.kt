package com.rizz.focusbox.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.rizz.focusbox.feature.home.navigation.homeScreen
import com.rizz.focusbox.feature.home.navigation.navigateToHomeScreen
import com.rizz.focusbox.feature.notificationpermission.navigation.navigateToNotificationRationaleScreen
import com.rizz.focusbox.feature.notificationpermission.navigation.notificationRationaleScreen
import com.rizz.focusbox.feature.onboarding.navigation.navigateToOnboardingScreen
import com.rizz.focusbox.feature.onboarding.navigation.onBoardingScreen
import com.rizz.focusbox.feature.splash.navigation.Splash
import com.rizz.focusbox.feature.splash.navigation.splashScreen

@Composable
fun FocusBoxNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Splash,
        modifier = modifier,
    ) {
        splashScreen(
            onNavigateToOnboarding = { navController.navigateToOnboardingScreen(navController.clearBackStackOptions()) },
            onNavigateToHome = { navController.navigateToHomeScreen(navController.clearBackStackOptions()) },
        )
        onBoardingScreen(
            onNavigateToNotificationRationale = { navController.navigateToNotificationRationaleScreen() },
            onFinished = { navController.navigateToHomeScreen(navController.clearBackStackOptions()) },
        )
        notificationRationaleScreen(
            onFinished = { navController.navigateToHomeScreen(navController.clearBackStackOptions()) },
        )
        homeScreen()
    }
}

private fun NavHostController.clearBackStackOptions(): NavOptions =
    navOptions {
        popUpTo(graph.startDestinationId) { inclusive = true }
    }
