package com.rizz.focusbox.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.rizz.focusbox.feature.home.view.HomeScreenMain
import kotlinx.serialization.Serializable

@Serializable
data object Home

fun NavController.navigateToHomeScreen(navOptions: NavOptions? = null) {
    navigate(route = Home, navOptions = navOptions)
}

fun NavGraphBuilder.homeScreen() {
    composable<Home> {
        HomeScreenMain()
    }
}
