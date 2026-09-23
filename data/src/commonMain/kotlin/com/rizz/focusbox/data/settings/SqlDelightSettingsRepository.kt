package com.rizz.focusbox.data.settings

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.rizz.focusbox.data.dao.SettingsDao
import com.rizz.focusbox.db.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single(binds = [SettingsRepository::class])
class SqlDelightSettingsRepository(
    private val settingsDao: SettingsDao,
) : SettingsRepository {

    init {
        settingsDao.insertDefaultSettings()
    }

    override fun observeTimerSettings(): Flow<TimerDurationsSettings> =
        settingsDao.selectSettings()
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { row -> row?.toSettings() ?: TimerDurationsSettings.DEFAULT }

    override fun updateTimerDurations(
        focusMin: Int,
        shortBreakMin: Int,
        longBreakMin: Int,
        longBreakInterval: Int,
    ) {
        settingsDao.updateTimerDurations(
            focusMin = focusMin.toLong(),
            shortBreakMin = shortBreakMin.toLong(),
            longBreakMin = longBreakMin.toLong(),
            longBreakInterval = longBreakInterval.toLong(),
        )
    }

    override fun updateNotificationsEnabled(enabled: Boolean) {
        settingsDao.updateNotificationsEnabled(if (enabled) 1L else 0L)
    }

    private fun Settings.toSettings(): TimerDurationsSettings =
        TimerDurationsSettings(
            focusMin = focusMin.toInt(),
            shortBreakMin = shortBreakMin.toInt(),
            longBreakMin = longBreakMin.toInt(),
            longBreakInterval = longBreakInterval.toInt(),
            notificationsEnabled = notificationsEnabled != 0L,
        )
}
