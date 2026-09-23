package com.rizz.focusbox.feature.notificationpermission.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.rizz.focusbox.feature.notificationpermission.view.NotificationRationaleScreenMain
import kotlinx.serialization.Serializable

@Serializable
data object NotificationRationale

fun NavController.navigateToNotificationRationaleScreen(navOptions: NavOptions? = null) {
    navigate(route = NotificationRationale, navOptions = navOptions)
}

fun NavGraphBuilder.notificationRationaleScreen(onFinished: () -> Unit) {
    composable<NotificationRationale> {
        NotificationRationaleScreenMain(onFinished = onFinished)
    }
}
