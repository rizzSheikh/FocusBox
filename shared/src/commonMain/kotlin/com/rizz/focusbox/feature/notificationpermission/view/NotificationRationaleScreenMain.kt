package com.rizz.focusbox.feature.notificationpermission.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.rizz.focusbox.feature.notificationpermission.rememberNotificationPermissionRequester
import com.rizz.focusbox.feature.notificationpermission.view.components.NotificationRationaleScreen
import com.rizz.focusbox.feature.notificationpermission.view.effect.NotificationRationaleScreenEffect
import com.rizz.focusbox.feature.notificationpermission.view.event.NotificationRationaleScreenEvents
import com.rizz.focusbox.feature.notificationpermission.viewmodel.NotificationRationaleViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NotificationRationaleScreenMain(
    onFinished: () -> Unit,
    viewModel: NotificationRationaleViewModel = koinViewModel(),
) {
    val requestNotificationPermission = rememberNotificationPermissionRequester { _ ->
        viewModel.onEvent(NotificationRationaleScreenEvents.OnPermissionResult)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                NotificationRationaleScreenEffect.NavigateHome -> onFinished()
            }
        }
    }

    NotificationRationaleScreen(
        onEnableNotifications = requestNotificationPermission,
        onNotNow = { viewModel.onEvent(NotificationRationaleScreenEvents.OnNotNowClick) },
    )
}
