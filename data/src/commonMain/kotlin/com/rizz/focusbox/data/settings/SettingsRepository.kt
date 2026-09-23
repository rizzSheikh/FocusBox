package com.rizz.focusbox.data.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun observeTimerSettings(): Flow<TimerDurationsSettings>

    fun updateTimerDurations(
        focusMin: Int,
        shortBreakMin: Int,
        longBreakMin: Int,
        longBreakInterval: Int,
    )

    fun updateNotificationsEnabled(enabled: Boolean)
}
