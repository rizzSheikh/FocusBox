package com.rizz.focusbox.data.settings

data class TimerDurationsSettings(
    val focusMin: Int,
    val shortBreakMin: Int,
    val longBreakMin: Int,
    val longBreakInterval: Int,
    val notificationsEnabled: Boolean,
) {
    companion object {
        val DEFAULT = TimerDurationsSettings(
            focusMin = 25,
            shortBreakMin = 5,
            longBreakMin = 15,
            longBreakInterval = 4,
            notificationsEnabled = true,
        )
    }
}
