package com.rizz.focusbox.feature.stats

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.rizz.focusbox.db.FocusBoxDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class SqlDelightStatsRepositoryTest {

    private fun newDatabase(): FocusBoxDatabase {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        FocusBoxDatabase.Schema.create(driver)
        return FocusBoxDatabase(driver)
    }

    @Test
    fun observeSessions_emits_inserted_rows() = runBlocking {
        val db = newDatabase()
        val repository = SqlDelightStatsRepository(db)

        db.focusSessionQueries.insertSession(
            taskName = "Write report",
            type = "FOCUS",
            plannedDurationSec = 1500,
            actualDurationSec = 1500,
            startedAt = 1000L,
            endedAt = 2500L,
            completed = 1L
        )

        val sessions = repository.observeSessions().first()

        assertEquals(1, sessions.size)
        assertEquals("Write report", sessions[0].taskName)
        assertEquals(1L, sessions[0].completed)
    }

    @Test
    fun observeSessions_on_empty_table_emits_empty_list() = runBlocking {
        val db = newDatabase()
        val repository = SqlDelightStatsRepository(db)

        val sessions = repository.observeSessions().first()

        assertEquals(emptyList(), sessions)
    }
}
