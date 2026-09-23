package com.rizz.focusbox.data.dao

import app.cash.sqldelight.Query
import com.rizz.focusbox.db.FocusBoxDatabase
import com.rizz.focusbox.db.FocusSession
import org.koin.core.annotation.Single

@Single
class FocusSessionDao(
    private val db: FocusBoxDatabase,
) {
    fun insertSession(
        taskName: String?,
        type: String,
        plannedDurationSec: Long,
        actualDurationSec: Long,
        startedAt: Long,
        endedAt: Long?,
        completed: Long,
    ) {
        db.focusSessionQueries.insertSession(
            taskName = taskName,
            type = type,
            plannedDurationSec = plannedDurationSec,
            actualDurationSec = actualDurationSec,
            startedAt = startedAt,
            endedAt = endedAt,
            completed = completed,
        )
    }

    fun selectAll(): Query<FocusSession> = db.focusSessionQueries.selectAll()
}
