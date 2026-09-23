package com.rizz.focusbox.data.timer

import com.rizz.focusbox.db.FocusBoxDatabase
import org.koin.core.annotation.Single

@Single(binds = [SessionRepository::class])
class SqlDelightSessionRepository(private val db: FocusBoxDatabase) : SessionRepository {
    override fun insertSession(session: FocusSessionRecord) {
        db.focusSessionQueries.insertSession(
            taskName = session.taskName,
            type = session.type,
            plannedDurationSec = session.plannedDurationSec,
            actualDurationSec = session.actualDurationSec,
            startedAt = session.startedAt,
            endedAt = session.endedAt,
            completed = if (session.completed) 1L else 0L
        )
    }
}
