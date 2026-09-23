package com.rizz.focusbox.feature.notificationpermission.view.event

sealed class NotificationRationaleScreenEvents {
    data object OnPermissionResult : NotificationRationaleScreenEvents()
    data object OnNotNowClick : NotificationRationaleScreenEvents()
}
