package com.rizz.focusbox.data.timer

data class FocusSessionRecord(
    val taskName: String?,
    val type: String,
    val plannedDurationSec: Long,
    val actualDurationSec: Long,
    val startedAt: Long,
    val endedAt: Long?,
    val completed: Boolean
)

interface SessionRepository {
    fun insertSession(session: FocusSessionRecord)
}
