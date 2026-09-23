package com.rizz.focusbox.data.dao

import app.cash.sqldelight.Query
import com.rizz.focusbox.db.FocusBoxDatabase
import com.rizz.focusbox.db.Settings
import org.koin.core.annotation.Single

@Single
class SettingsDao(private val db: FocusBoxDatabase) {
    fun insertDefaultSettings() {
        db.settingsQueries.insertDefaultSettings()
    }

    fun selectSettings(): Query<Settings> = db.settingsQueries.selectSettings()

    fun updateTimerDurations(
        focusMin: Long,
        shortBreakMin: Long,
        longBreakMin: Long,
        longBreakInterval: Long,
    ) {
        db.settingsQueries.updateTimerDurations(
            focusMin = focusMin,
            shortBreakMin = shortBreakMin,
            longBreakMin = longBreakMin,
            longBreakInterval = longBreakInterval,
        )
    }

    fun updateNotificationsEnabled(enabled: Long) {
        db.settingsQueries.updateNotificationsEnabled(enabled)
    }
}
