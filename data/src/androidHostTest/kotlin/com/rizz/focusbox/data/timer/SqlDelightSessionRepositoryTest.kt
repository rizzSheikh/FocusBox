package com.rizz.focusbox.data.timer

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.rizz.focusbox.data.dao.FocusSessionDao
import com.rizz.focusbox.db.FocusBoxDatabase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SqlDelightSessionRepositoryTest {

    private fun newDatabase(): FocusBoxDatabase {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        FocusBoxDatabase.Schema.create(driver)
        return FocusBoxDatabase(driver)
    }

    @Test
    fun insertSession_persists_a_completed_focus_session() {
        val db = newDatabase()
        val repository = SqlDelightSessionRepository(FocusSessionDao(db))

        repository.insertSession(
            FocusSessionRecord(
                taskName = "Write report",
                type = "FOCUS",
                plannedDurationSec = 1500,
                actualDurationSec = 1500,
                startedAt = 1000L,
                endedAt = 2500L,
                completed = true
            )
        )

        val rows = db.focusSessionQueries.selectAll().executeAsList()
        assertEquals(1, rows.size)
        assertEquals("Write report", rows[0].taskName)
        assertEquals("FOCUS", rows[0].type)
        assertEquals(1500L, rows[0].plannedDurationSec)
        assertEquals(1500L, rows[0].actualDurationSec)
        assertEquals(1000L, rows[0].startedAt)
        assertEquals(2500L, rows[0].endedAt)
        assertEquals(1L, rows[0].completed)
    }

    @Test
    fun insertSession_persists_a_skipped_session_with_null_task_name() {
        val db = newDatabase()
        val repository = SqlDelightSessionRepository(FocusSessionDao(db))

        repository.insertSession(
            FocusSessionRecord(
                taskName = null,
                type = "SHORT_BREAK",
                plannedDurationSec = 300,
                actualDurationSec = 120,
                startedAt = 1000L,
                endedAt = 1120L,
                completed = false
            )
        )

        val row = db.focusSessionQueries.selectAll().executeAsList().single()
        assertNull(row.taskName)
        assertEquals(0L, row.completed)
    }
}
