package com.rizz.focusbox.data.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun observeTimerSettings(): Flow<TimerDurationsSettings>
}
