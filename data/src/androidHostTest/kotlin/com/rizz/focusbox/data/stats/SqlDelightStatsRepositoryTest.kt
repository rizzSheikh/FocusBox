package com.rizz.focusbox.data.stats

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.rizz.focusbox.data.dao.FocusSessionDao
import com.rizz.focusbox.db.FocusBoxDatabase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
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
        val repository = SqlDelightStatsRepository(FocusSessionDao(db))

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
        val repository = SqlDelightStatsRepository(FocusSessionDao(db))

        val sessions = repository.observeSessions().first()

        assertEquals(emptyList(), sessions)
    }

    @Test
    fun observeSessions_emits_a_new_value_when_a_row_is_inserted_after_collection_starts() = runBlocking {
        val db = newDatabase()
        val repository = SqlDelightStatsRepository(FocusSessionDao(db))

        db.focusSessionQueries.insertSession(
            taskName = "Write report",
            type = "FOCUS",
            plannedDurationSec = 1500,
            actualDurationSec = 1500,
            startedAt = 1000L,
            endedAt = 2500L,
            completed = 1L
        )

        // Subscribe once and keep the SAME flow collection alive across the second insert,
        // proving SQLDelight's change-notification re-emits on the existing collector
        // (as opposed to two independent fresh reads from two separate collections).
        val firstEmissionReceived = CompletableDeferred<Unit>()
        val emissionsDeferred = async {
            repository.observeSessions()
                .onEach { if (!firstEmissionReceived.isCompleted) firstEmissionReceived.complete(Unit) }
                .take(2)
                .toList()
        }

        // Wait for the initial emission before mutating the table, so the second insert
        // is guaranteed to happen while the collector is already subscribed.
        firstEmissionReceived.await()

        db.focusSessionQueries.insertSession(
            taskName = "Read book",
            type = "FOCUS",
            plannedDurationSec = 1500,
            actualDurationSec = 1500,
            startedAt = 3000L,
            endedAt = 4500L,
            completed = 1L
        )

        val emissions = emissionsDeferred.await()

        assertEquals(2, emissions.size)
        assertEquals(1, emissions[0].size)
        assertEquals(2, emissions[1].size)
    }
}
