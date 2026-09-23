package com.rizz.focusbox.feature.notificationpermission

import androidx.compose.runtime.Composable

@Composable
expect fun rememberNotificationPermissionRequester(onResult: (granted: Boolean) -> Unit): () -> Unit
