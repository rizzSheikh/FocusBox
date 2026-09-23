package com.rizz.focusbox.feature.notificationpermission

import androidx.compose.runtime.Composable

@Composable
actual fun rememberNotificationPermissionRequester(onResult: (granted: Boolean) -> Unit): () -> Unit =
    { onResult(true) }
