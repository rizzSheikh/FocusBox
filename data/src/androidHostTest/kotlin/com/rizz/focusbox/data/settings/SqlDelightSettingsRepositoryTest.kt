package com.rizz.focusbox.data.settings

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.rizz.focusbox.data.dao.SettingsDao
import com.rizz.focusbox.db.FocusBoxDatabase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SqlDelightSettingsRepositoryTest {

    private fun newDatabase(): FocusBoxDatabase {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        FocusBoxDatabase.Schema.create(driver)
        return FocusBoxDatabase(driver)
    }

    @Test
    fun init_inserts_default_settings_row() {
        val db = newDatabase()
        SqlDelightSettingsRepository(SettingsDao(db))

        val row = db.settingsQueries.selectSettings().executeAsOneOrNull()
        assertNotNull(row)
        assertEquals(25L, row.focusMin)
        assertEquals(5L, row.shortBreakMin)
        assertEquals(15L, row.longBreakMin)
        assertEquals(4L, row.longBreakInterval)
        assertEquals(1L, row.notificationsEnabled)
    }

    @Test
    fun updateTimerDurations_persisted_in_database() {
        val db = newDatabase()
        SqlDelightSettingsRepository(SettingsDao(db))
        SettingsDao(db).updateTimerDurations(
            focusMin = 30,
            shortBreakMin = 10,
            longBreakMin = 20,
            longBreakInterval = 3,
        )

        val row = db.settingsQueries.selectSettings().executeAsOneOrNull()
        assertNotNull(row)
        assertEquals(30L, row.focusMin)
        assertEquals(10L, row.shortBreakMin)
        assertEquals(20L, row.longBreakMin)
        assertEquals(3L, row.longBreakInterval)
    }
}
